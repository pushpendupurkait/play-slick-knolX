package models

import play.api.db.DB
import play.api.db.slick.Config.driver.simple._
import controllers._
import play.api.Play.current
import play.api.db.slick._
import scala.slick.lifted.Tag
import java.sql.Timestamp
import java.util.Date
import scala.slick.lifted.TableQuery



class UserTable(tag: Tag) extends Table[User](tag, "usertable") {

  implicit val util2sqlDateMapper = MappedColumnType.base[java.util.Date, java.sql.Date](
    { utilDate => new java.sql.Date(utilDate.getTime()) },
    { sqlDate => new java.util.Date(sqlDate.getTime()) })
    
  /**
   * Table description and columns 
   */

  def Name: Column[String] = column[String]("Name", O.NotNull)
  def Address: Column[String] = column[String]("Address")
  def Company: Column[String] = column[String]("Company")
  def Email: Column[String] = column[String]("Email", O.NotNull)
  def Password: Column[String] = column[String]("Password", O.NotNull)
  def Phone: Column[String] = column[String]("Phone")
  def UserType: Column[Int] = column[Int]("UserType", O.NotNull)
  def id: Column[Int] = column[Int]("id", O.PrimaryKey, O.AutoInc)
  def created = column[Date]("created")
  def updated = column[Date]("updated")
  def uniqueName = index("IDX_EMAIL", Email, unique = true)
  def * = (Name, Address, Company, Email, Password, UserType, Phone, id, created, updated) <> (User.tupled, User.unapply)
} 

object UserTable {

  val userTbl = TableQuery[UserTable]
  
  /**
   * signUp inserts data in table
   */

  def signUp(user: User)(implicit s: Session): Int = {
    //userTbl.ddl.create;
    val afftdRow = userTbl.insert(user)
    afftdRow
  }
  /**
   * signIn takes data from table to check Email and Password at time of Sign In
   */
  
  def signIn(signin:SignIn)(implicit s: Session):Int={
    val afftdRow =Query(userTbl.filter(_.Email===signin.Email).filter(_.Password===signin.Password).length).first
    afftdRow
  }
  
  /**
   * update changes user's data as per user's need
   */
  
  def update(user:User,email:String)(implicit s:Session):Int={
    val afftdRow = userTbl.filter(_.Email===email).update(user)
    afftdRow
  }
  
  /**
   * getRecordByEmail takes email as argument and returns one record 
   */
  
  def getRecordByEmail(email:String)(implicit s:Session):(User)={
    userTbl.filter(_.Email===email).list.head
  }

}