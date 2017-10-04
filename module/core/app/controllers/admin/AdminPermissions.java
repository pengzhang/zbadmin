package controllers.admin;


import annotations.Check;
import annotations.For;
import annotations.Login;
import controllers.AdminActionIntercepter;
import controllers.CRUD;
import controllers.Secure;
import models.core.adminuser.Permission;
import play.mvc.With;

@Login
@Check("")
@For(Permission.class)
@With({AdminActionIntercepter.class,Secure.class})
public class AdminPermissions extends CRUD{

}
