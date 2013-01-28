# Play 2.0

Staticly-typed Web Development

Tony Baines
January 2013

.fx: titleslide

---

# Web development in Java

* Static-typing on the server side
* URL paths
* JSP variables (what's in scope?)

---

# Web development in Rails

## Convention over configuration
* Structure (MVC)
* Database
* URLs
* ...

---
# Scala 

* SCAlable LAnguage
* Statically-typed
* Object-oriented plus functional ideas

---

# Play 2.0

* Compile-time checking + conventions
* Internal App URL paths
* Page Templates are functions
* ... with typed parameters

---
# Some interesting code here

    !scala
    class TheatreController 
                    extends AbstractActionController {
        protected $theatreMapper;

        public function indexAction()
        {
            $mapper = $this->getTheatreMapper();
            $theatres = $mapper->fetchAll();

            return new ViewModel(array('theatres' => $theatres));
        }
    }

---
# Built for asynchronous programming

Today’s web applications are integrating more concurrent real-time data, so web frameworks need to support a full asynchronous HTTP programming model. Play was initially designed to handle classic web applications with many short-lived requests. But now, the event model is the way to go for persistent connections - though Comet, long-polling and WebSockets.

Play 2.0 is architected from the start under the assumption that every request is potentially long-lived. But that’s not all: we also need a powerful way to schedule and run long-running tasks. The Actor-based model is unquestionably the best model today to handle highly concurrent systems, and the best implementation of that model available for both Java and Scala is Akka - so it’s going in. Play 2.0 provides native Akka support for Play applications, making it possible to write highly-distributed systems.

---
# Focused on type safety

One benefit of using a statically-typed programming language for writing Play applications is that the compiler can check parts of your code. This is not only useful for detecting mistakes early in the development process, but it also makes it a lot easier to work on large projects with a lot developers involved.

In templates, Scala is mainly used to navigate your object graph in order to display relevant information, with a syntax that is very close to Java’s. However, if you want to unleash the power of Scala to write advanced templates abstractions, you will quickly discover how Scala, being expression-oriented and functional, is a perfect fit for a template engine.

And that’s not only true for the template engine: the routing system is also fully type-checked. Play 2.0 checks your routes’ descriptions, and verifies that everything is consistent, including the reverse routing part.

---
# Build System

From the beginning of the Play project, we have chosen a fresh way to run, compile and deploy Play applications. It may have looked like an esoteric design at first, but it was crucial to providing an asynchronous HTTP API instead of the standard Servlet API, short feedback cycles through live compilation and reloading of source code during development, and promoting a fresh packaging approach. Consequently, it was difficult to make Play follow the standard JEE conventions.

Today, this idea of container-less deployment is increasingly accepted in the Java world. It’s a design choice that has allowed the Play framework to run natively on platforms like Heroku, which introduced a model that we consider the future of Java application deployment on elastic PaaS platforms.

---
# Datastore and model integration

‘Data store’ is no longer synonymous with ‘SQL database’, and probably never was. A lot of interesting data storage models are becoming popular, providing different properties for different scenarios. For this reason it has become difficult for a web framework like Play to make bold assumptions regarding the kind of data store that developers will use. A generic model concept in Play no longer makes sense, since it is almost impossible to abstract over all these kinds of technologies with a single API.

In Play 2.0, we wanted to make it really easy to use any data store driver, ORM, or any other database access library without any special integration with the web framework. We simply want to offer a minimal set of helpers to handle common technical issues, like managing the connection bounds. We also want, however, to maintain the full-stack aspect of Play framework by bundling default tools to access classical databases for users WHO don’t have specialized needs, and that’s why Play 2.0 comes with built-in relational database access libraries such as Ebean, JPA and Anorm.

---
# Play Console

* Compile
* Run
* Reload (not recompile / restart)
  * Compile errors in the web console

---
# Routes

A Scala DSL

Edit the conf/routes file:

    !scala
    # Home page
    GET     /                       controllers.Application.index
                                
    # Tasks          
    GET     /tasks                  controllers.Application.tasks
    POST    /tasks                  controllers.Application.newTask
    POST    /tasks/:id/delete       controllers.Application.deleteTask(id: Long)


Reverse routing e.g. redirect to the tasks list page:

    !scala
    def index = Action {
      Redirect(routes.Application.tasks)
    }

---

# Forms

A Form object encapsulates an HTML form definition, including validation constraints. Let’s create a very simple form in the Application controller: we only need a form with a single label field. The form will also check that the label provided by the user is not empty:

    !scala
    import play.api.data._
    import play.api.data.Forms._

    val taskForm = Form(
      "label" -> nonEmptyText
    )

---
# Handling the form submission

For now, if we submit the task creation form, we still get the TODO page. Let’s write the implementation of the newTask action:

    !scala
    def newTask = Action { implicit request =>
      taskForm.bindFromRequest.fold(
        errors => BadRequest(views.html.index(Task.all(), errors)),
        label => {
          Task.create(label)
          Redirect(routes.Application.tasks)
        }
      )
    }

To fill the form we need to have the request in the scope, so it can be used by bindFromRequest to create a new form filled with the request data. If there are any errors in the form, we redisplay it (here we use 400 Bad Request instead of 200 OK). If there are no errors, we create the task and then redirect to the task list.


---
# Templates

Play templates are compiled to standard Scala functions, here as views.html.index(message: String).

This template is defined in the app/views/index.scala.html source file:

	!scala
	@(message: String)

	@main("Welcome to Play 2.0") {
	    @play20.welcome(message)
	}

The first line defines the function signature. Here it takes a single String parameter. Then the template content mix HTML (or any text based language) with Scala statements. The Scala statements starts with the special @ character.

---
# Database Schema Evolutions

evolution script in conf/evolutions/default/1.sql:

    !sql
    # Tasks schema
     
    # --- !Ups
    
    CREATE SEQUENCE task_id_seq;
    CREATE TABLE task (
        id integer NOT NULL DEFAULT nextval('task_id_seq'),
        label varchar(255)
    );
 
    # --- !Downs
 
    DROP TABLE task;
    DROP SEQUENCE task_id_seq;

---
# Action Composition

Actions (i.e. the methods that are invoked by clicking a link or pushing a button) are composable, which means you can take one, and wrap it around another, and end up with a new action that is a combination of the two. This allows for powerful and scaling abstractions.

The basic action is called Action (no surprise here) and is essentially a function which maps a request to a result. For example, in our application we have pages that can only be accessed by an administrator. So the easiest way to protect those pages was to define an action called AuthenticatedAdmin which in turns calls Authenticated (whose only job is it to verify if the user is logged in), which in turn calls the actual action.

In terms of code it looks like this:

    !scala
    def Authenticated[A](p: BodyParser[A])(f: AuthenticatedRequest[A] => Result) = {
      Action(p) { implicit request =>
        request.session.get("email").flatMap(email => UserDAO.findByEmail(email)).map { user =>
          f(AuthenticatedRequest(user, request))
        }.getOrElse(Results.Ok(views.html.error("Not Authorized", "You have to log in to view the    requested page")))
      }
    }

    def AuthenticatedAdmin[A](f: AuthenticatedRequest[AnyContent] => Result) = Authenticated { implicit request =>
      request.user.adminProfile match {
        case Some(profile: AdminProfile) => f(AuthenticatedRequest(request.user, request))
        case _ => Results.Ok(views.html.error("Not Authorized", "You need administrator privileges to access the requested page"))
      }
    }

The Authenticated action reads the email from the session cookie. If the email is not there, the user has to log-in (which is an overlay in our app, otherwise you’d redirect to the log-in page). If the email is found it is used to find the actual user object (from the database or the cache) and put it in the request (a custom request called AuthenticatedRequest, essentially a case class containing the real request and the user). Now the AuthenticatedAdmin action can reuse these mechanics and add another layer around it. It only checks if the user has an admin profile (which is a simple role system of course) and only then execute the action function (called f) itself.

Finally we can use it in the controller like this:

    !scala
    def index = AuthenticatedAdmin { implicit request =>
      Ok(views.html.admin.index())
    }

---
# Thank you



