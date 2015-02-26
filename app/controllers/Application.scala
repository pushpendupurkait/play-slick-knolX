package controllers

import play.api._
import play.api.db.DB
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import play.api.db.slick._
import play.api.Play.current
import play.api.data.format.Formats._
import play.api.db.slick.Config.driver.simple._
import play.api.data.Form
import play.api.db.slick.DBAction
import play.twirl.api.Html
import views._
import models._
import play.mvc.Results.Redirect
import scala.slick.lifted.TableQuery
import play.mvc.Results.Redirect
import java.util.Date

object Application extends Controller {

  /**
   * Index for index page
   */
  def index = Action { request =>
    request.session.get("EmailID").map { Email =>
      Ok(views.html.dashboard(Email))
    }.getOrElse {
      Ok(views.html.signInForm(signInForm))
    }
  }
  
  /**
   * signUpPage is for Sign Up Form
   */
  
  def signUpPage = Action { request =>
    request.session.get("EmailID").map { Email =>
      Ok(views.html.dashboard(Email))
    }.getOrElse {
      Ok(views.html.signUpForm(signUpForm))
    }
  }
  
  def signInPage = Action { request =>
    request.session.get("EmailID").map { Email =>
      Ok(views.html.dashboard(Email))
    }.getOrElse {
      Ok(views.html.signInForm(signInForm))
    }
  }
  
  def contact = Action{
    Ok(views.html.contact("Contact"))
  }
  
  def logout = Action { request =>
    Ok(views.html.signInForm(signInForm)).withNewSession
  }
  
  def updatePage = Action { request =>
    request.session.get("EmailID").map { Email =>
      Ok(views.html.updateForm(signUpForm))
    }.getOrElse {
      Ok(views.html.signInForm(signInForm))
    }
  }
  
  def dashboard = Action {
    Ok(views.html.dashboard("Update"))
  }
  
  /**
   * signUp works for signUp functionality
   * It inserts data in table
   */

  def signUp: Action[AnyContent] = DBAction { implicit rs =>
    try {
      val success = signUpForm.bindFromRequest.get
      val afftdRow = UserTable.signUp(success)
      Ok(views.html.signInForm(signInForm))
    } catch {
      case ex: Exception =>
        Ok(views.html.signUpForm(signUpForm))
    }
  }
  
  /**
   * signIn works for signIn functionality
   */
  
  def signIn: Action[AnyContent] = DBAction { implicit rs =>
    try {
      val success = signInForm.bindFromRequest.get
      val count = UserTable.signIn(success)
      if (count > 0) {
        Ok(views.html.dashboard(success.Email)).withSession("EmailID" -> success.Email)
      } else {
        Ok("Logged In Not Done")
      }
    } catch {
      case ex: Exception =>
        Ok(views.html.signInForm(signInForm))
    }
  }
  
  /**
   * update works to make changes in users data
   * 
   */
  
  def update: Action[AnyContent] = DBAction { implicit rs =>

    val data = rs.session.get("EmailID").map {
      Email =>
        {
          val oldRecord = UserTable.getRecordByEmail(Email)
          val formData = signUpForm.bindFromRequest.get
          val dataToUpdate: User = formData.copy(id = oldRecord.id, Created = oldRecord.Created)
          val afftdRow = UserTable.update(dataToUpdate, Email)
          dataToUpdate
        }
    }
    
    val dataForNewSession = data.get
    Ok(views.html.dashboard(dataForNewSession.Email)).withSession(
      "EmailID" -> dataForNewSession.Email)
  }
  
  /**
   * showInfo works to show user's data on screen.
   */
  def showInfo: Action[AnyContent] = DBAction { implicit rs =>
    try {
      val data = rs.session.get("EmailID").map { Email =>
        UserTable.getRecordByEmail(Email)
      }.get
      Ok(views.html.showInformation(data))
    } catch {
      case ex: Exception =>
        Ok(views.html.signInForm(signInForm))
    }
  }

  /**
   * signUpForm is form to take data from user.
   */
  val signUpForm = Form(
    mapping(
      "Name" -> nonEmptyText,
      "Address" -> nonEmptyText,
      "Company" -> nonEmptyText,
      "Email" -> nonEmptyText,
      "Password" -> nonEmptyText,
      "UserType" -> ignored(2),
      "Phone" -> nonEmptyText,
      "id" -> ignored(1),
      "Created" -> ignored(new Date),
      "Updated" -> ignored(new Date))(User.apply)(User.unapply))

      
  /**
   * signInForm takes Email and Password from user to sign In
   */
  val signInForm = Form(
    mapping(
      "Email" -> nonEmptyText,
      "Password" -> nonEmptyText)(SignIn.apply)(SignIn.unapply))

}

case class SignIn(Email: String, Password: String)
case class User(Name: String, Address: String, Company: String, Email: String, Password: String, UserType: Int, Phone: String, id: Int, Created: Date, Update: Date)
