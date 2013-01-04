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
* 

---
# Some interesting code here

    !java
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
# Thank you



