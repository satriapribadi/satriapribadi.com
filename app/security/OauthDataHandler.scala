package security

import javax.inject.{Inject, Singleton}

import models.{OauthAccessToken, User}
import play.api.db.slick.DatabaseConfigProvider
import repositories.{OauthAccessTokenRepository, OauthAuthorizationCodeRepository, OauthClientRepository, UserRepository}
import utils.Constants

import scala.concurrent.{ExecutionContext, Future}
import scalaoauth2.provider.{ClientCredentialsRequest, InvalidClient, PasswordRequest, _}

/**
  * Created by Satria Pribadi on 12/07/16.
  */
@Singleton
class OauthDataHandler @Inject()
(protected val dbConfigProvider: DatabaseConfigProvider,
 userRepository: UserRepository,
 oauthClientRepository: OauthClientRepository,
 oauthAccessTokenRepository: OauthAccessTokenRepository,
 oauthAuthorizationCodeRepository: OauthAuthorizationCodeRepository)
(implicit ec: ExecutionContext) extends DataHandler[User] {

  private val accessTokenExpireSeconds = 86400

  private def toAccessToken(accessToken: OauthAccessToken) = {
    AccessToken(
      accessToken.accessToken,
      Some(accessToken.refreshToken),
      None,
      Some(accessTokenExpireSeconds),
      accessToken.createdAt.toDate
    )
  }

  override def validateClient(request: AuthorizationRequest): Future[Boolean] = {
    request.clientCredential.fold(Future.successful(false))(c => oauthClientRepository.validate(c.clientId, c.clientSecret.getOrElse(Constants.Global.EMPTY_STRING), request.grantType))
  }

  override def getStoredAccessToken(authInfo: AuthInfo[User]): Future[Option[AccessToken]] = {
    oauthAccessTokenRepository.findByAuthorized(authInfo.user, authInfo.clientId.getOrElse(Constants.Global.EMPTY_STRING)).map(_.map(toAccessToken))
  }

  override def findAuthInfoByAccessToken(accessToken: AccessToken): Future[Option[AuthInfo[User]]] = {
    oauthAccessTokenRepository.findByAccessToken(accessToken.token).flatMap {
      case Some(token) =>
        for {
          account <- userRepository.findById(Some(token.userId))
          client <- oauthClientRepository.findById(Some(token.oauthClientId))
        } yield {
          Some(AuthInfo(
            user = account.get,
            clientId = Some(client.get.clientId),
            scope = None,
            redirectUri = None
          ))
        }
      case None => Future.failed(new InvalidRequest())
    }
  }

  override def findAccessToken(token: String): Future[Option[AccessToken]] = oauthAccessTokenRepository.findByAccessToken(token).map(_.map(toAccessToken))

  override def createAccessToken(authInfo: AuthInfo[User]): Future[AccessToken] = {
    authInfo.clientId.fold(Future.failed[AccessToken](new InvalidRequest())) { clientId =>
      (for {
        clientOpt <- oauthClientRepository.findByClientId(clientId)
        toAccessToken <- oauthAccessTokenRepository.create(authInfo.user, clientOpt.get).map(toAccessToken) if clientOpt.isDefined
      } yield toAccessToken).recover { case _ => throw new InvalidRequest() }
    }
  }

  override def refreshAccessToken(authInfo: AuthInfo[User], refreshToken: String): Future[AccessToken] = {
    authInfo.clientId.fold(Future.failed[AccessToken](new InvalidRequest())) { clientId => (
      for {
        clientOpt <- oauthClientRepository.findByClientId(clientId)
        toAccessToken <- oauthAccessTokenRepository.refresh(authInfo.user, clientOpt.get).map(toAccessToken) if clientOpt.isDefined
      } yield toAccessToken).recover { case _ => throw new InvalidClient() }
    }
  }

  override def findAuthInfoByRefreshToken(refreshToken: String): Future[Option[AuthInfo[User]]] = {
    oauthAccessTokenRepository.findByRefreshToken(refreshToken).flatMap {
      case Some(accessToken) =>
        for {
          account <- userRepository.findById(Some(accessToken.userId))
          client <- oauthClientRepository.findById(Some(accessToken.oauthClientId))
        } yield {
          Some(AuthInfo(
            user = account.get,
            clientId = Some(client.get.clientId),
            scope = None,
            redirectUri = None
          ))
        }
      case None => Future.failed(new InvalidRequest())
    }
  }

  override def findAuthInfoByCode(code: String): Future[Option[AuthInfo[User]]] = {
    oauthAuthorizationCodeRepository.findByCode(code).flatMap {
      case Some(c) =>
        for {
          account <- userRepository.findById(Some(c.accountId))
          client <- oauthClientRepository.findById(Some(c.oauthClientId))
        } yield {
          Some(AuthInfo(
            user = account.get,
            clientId = Some(client.get.clientId),
            scope = None,
            redirectUri = None
          ))
        }
      case None => Future.failed(new InvalidRequest(description = "Unauthorized"))
    }
  }

  override def findUser(request: AuthorizationRequest): Future[Option[User]] = {
    request match {
      case request: PasswordRequest =>
        userRepository.authenticate(request.username, request.password)
      case request: ClientCredentialsRequest =>
        request.clientCredential.fold(Future.failed[Option[User]](new InvalidRequest())) { clientCredential =>
          for {
            maybeUser <- oauthClientRepository.findClientCredentials(
              clientCredential.clientId,
              clientCredential.clientSecret.getOrElse(Constants.Global.EMPTY_STRING)
            )
          } yield maybeUser
        }
      case _ => Future.successful(None)
    }
  }

  override def deleteAuthCode(code: String): Future[Unit] = oauthAuthorizationCodeRepository.delete(code).map(_ => {})
}
