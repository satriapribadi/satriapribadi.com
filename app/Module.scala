import com.google.inject.AbstractModule
import repositories.impl.{OauthAccessTokenRepositoryImpl, OauthAuthorizationCodeRepositoryImpl, OauthClientRepositoryImpl, UserRepositoryImpl}
import repositories.{OauthAccessTokenRepository, OauthAuthorizationCodeRepository, OauthClientRepository, UserRepository}
import services.UserService
import services.impl.UserServiceImpl

/**
  * This class is a Guice module that tells Guice how to bind several
  * different types. This Guice module is created when the Play
  * application starts.
  *
  * Play will automatically use any class called `Module` that is in
  * the root package. You can create modules in other locations by
  * adding `play.modules.enabled` settings to the `application.conf`
  * configuration file.
  */
class Module extends AbstractModule {

  override def configure() = {
    // Ask Guice to create an instance of ApplicationTimer when the
    // application starts.
    //bind(classOf[ApplicationTimer]).asEagerSingleton()

    /* Services */
    bind(classOf[UserService]).to(classOf[UserServiceImpl])
    /* Repositories */
    bind(classOf[UserRepository]).to(classOf[UserRepositoryImpl])
    bind(classOf[OauthAccessTokenRepository]).to(classOf[OauthAccessTokenRepositoryImpl])
    bind(classOf[OauthAuthorizationCodeRepository]).to(classOf[OauthAuthorizationCodeRepositoryImpl])
    bind(classOf[OauthClientRepository]).to(classOf[OauthClientRepositoryImpl])
  }

}
