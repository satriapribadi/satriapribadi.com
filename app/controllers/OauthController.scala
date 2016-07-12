package controllers

import javax.inject._

import play.api.mvc._
import security.OauthDataHandler

import scala.concurrent.ExecutionContext
import scalaoauth2.provider._

/**
  * Created by Satria Pribadi on 12/07/16.
  */
@Singleton
class OauthController @Inject()(oauthDataHandler: OauthDataHandler)(implicit exec: ExecutionContext) extends Controller with OAuth2Provider {

  override val tokenEndpoint = new TokenEndpoint {
    override val handlers = Map(
      OAuthGrantType.REFRESH_TOKEN -> new RefreshToken(),
      OAuthGrantType.CLIENT_CREDENTIALS -> new ClientCredentials(),
      OAuthGrantType.PASSWORD -> new Password()
    )
  }

  def accessToken = Action.async { implicit request =>
    issueAccessToken(oauthDataHandler)
  }

}
