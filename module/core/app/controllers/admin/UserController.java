package controllers.admin;


import annotations.Check;
import annotations.For;
import annotations.Login;
import controllers.AdminActionIntercepter;
import controllers.CRUD;
import controllers.Secure;
import models.core.user.User;
import play.mvc.With;

@Login
@Check("")
@For(User.class)
@With({AdminActionIntercepter.class,Secure.class})
public class UserController extends CRUD{

}
