# My notes

**7/1/24**

Interface: declares methods that multiple classes can share. 

abstract keyword: declares a class, showing that some of its methods are still
needing to have 'implements' keyword called by a subclass.

subclasses: uses keyword 'extends' to add methods/variables to superclass
_constructor:_ uses super() to call the superclass constructor

shallow copy: creates a new reference to the data object

deep copy: creates a completely new data object. DO NOT confuse shallow and deep copying!
syntax: public Object clone() {return new Object(this)}


**7/10/24: PRINCIPLES OF DESIGN**
1. Design is inherently iterative! 
   1. You can never design 100% before you start coding. 
   2. You should try and design and build in a synergistic loop.
2. Abstraction
   1. Our job is to design a new language, in a way, that raises the level of abstraction to the thing you're working on. Rather than representing, say, DNA with string classes, you should create a DNA class that fits what DNA is better.
   2. You should be able to do something without understanding how it works internally.
   3. Since classes are usually complex, real-world things, you must be wise about what to include and what to leave out.
3. Good naming
   1. Try and name something exactly what it does
4. Single-responsibility principle
   1. each function/variable should only do one thing!
5. Decomposition
   1. Break longer functions into smaller ones (20-50 lines is pretty typical)
   2. Keep related methods/functions/variables close by
   3. Don't break it up so much that your code is unreadable
   4. system -> subsystem (client, server) -> packages -> classes -> methods
6. Good algorithm & data structure selection
7. Low Coupling 
   1. You want a class to minimally interact with other classes
   2. Avoid unnecessary dependencies between classes/functions
   3. "write shy code"
   4. _all_ internal implementation should be "private" unless there's a very good reason to make it "protected" or "public"
   5. Don't let internal details "leak out" of a class...use "ClassRoll" instead of "StudentLinkedList"
   6. code to interfaces
8. Avoid code duplication

__________________________________________
Web API - set of functions used across the web, anyone in the world could use http to call them
+ this is a great way to unify disparate systems
Most websites are actually both a Site and an API; the API calls functions from the other system to make things actually work behind the scenes

Model - the "heart and soul" of an application; contains the core things that model whatever the real world thing is you're doing.

DAO - the classes that access the database (no other anything should access the database)

CRUD methods - Create, Read (returns data from db), Update, Delete

**7/12/24**

_Generic Types_
+ allow you go specify one or more generic types when creating a class/interface
+ usually specified with single capital letters, e.g. 
`````````
class Pair<T,U> 
`````````
You can also instantiate without having to repeat the type twice:
``````
Pair<S,I> myPair = new Pair<>("yeet",5)
// or with keyword var
var myPair = new Pair<S,I>("yeet",5)
``````

You can specify the exact type if you want 
```
Pair<String String> hsh = new Pair<>("sss","fas")
```
You can use wildcards "?" to expand the acceptable types.
```
public void method(List<? super T>)    // takes type T or any of its super classes
public void method2(List<extends T>    // takes type T or any of its subclasses

```
_Lambda functions_

syntax:
```
(parameters (don't need to be typed)) -> {execute code, return if needed}
```
you don't need the curly braces if all you're doing is evaluating an expression and returning something
you don't need the () for parameters if you only have 1 parameter

You could store a lambda function in a variable if needed:
```
var r = () -> {your code here}
```

_HTTP stuff_

client specifies IP address via domain name (.com) to access a server.

client specifies the port # (of programs in the server) to tell the server what program to run.

https://www.google.com:443/images/branding/googlelogo/1x/googlelogo_color_272x92dp.png?d=val&c=sss#sgh
protocol  domain/IP   port#          path (sometimes a file, more often just a string)  query    fragment (links to part of a page)


http request:
METHOD  /path  HTTP version
headers
\n
request body

http response:
HTTP version  status code  reason phrase
headers
\n
response body (bytes in any format)

_cURL: client for URLs_

+ easy experimentation with HTTP endpoints
+ available everywhere
+ you can use it from the command line and make http requests

Another alternative is Postman, a GUI for curl stuff

**7/15/24**

_a Guide to Web API_
Spark: open-source framework for building Java web apps and web APIs

see: sparkjava.com


How to code a very basic server:

```
import spark.Spark
import spark.Route
import spark.Request
import spark.Response

public class SimpleHelloServer{
   public static void main(String[] args) {
      Spark.get("/hello", new HelloHandler());
      }
}

class HelloHandler implements Route {
   
   public Object handle(Request req, Response res) {
      return "Hello, BYU!"
   }
}
```

or, you could do it with a method request.

```
import spark.Spark
import spark.Route
import spark.Request
import spark.Response

public class SimpleHelloServer{
   public static void main(String[] args) {
      Spark.get("/hello", SimpleHelloServer::helloHandle());
      }
   public static String helloHandle(Request req, Response res){
      return "Hello BYU!"
      }
}
```
Routes:

```
get("/", (request, response) -> {
   // something
});
```

3 Ways to pass inputs into the handler: URL, header, body

URL
```
get("/hello:name", (request, response) -> {
   return "hello" + :name
});
```
Splat parameters (match anything in the /*/)
```
get("/hello/*/darling/*", (request, response) -> {
   return "hello" 
});
```

Response methods:
```
res.status(status code; 200, 401 etc)
res.type
res.header
res.body
```
How to declare what files your website will use:
(this is relative to the resources folder)
(must be done before mapping routes)
```
Spark.staticFiles.location("/public")
```
Filters: a way to execute common code for multiple routes without duplicating your code
```
before((request, response) -> 
   boolean authenticated;
   
   // check if authenticated
   
   if (!authenticated) {
   halt(401, "you ain't welcome here!") // stop it from getting to the thing
   }
  )
```
You can also have after() filters as well

**7/17/24 - Good Coding Practice!**

+ Cohesion: a method should do one and one thing only. 
+ Method name: this should completely and clearly describe what the method does. 
  + If the name is too long, that's a sign that you need to break up the function
  + If it's void, you should make it a verb. If non void, it could be a verb or what it returns.
  + establish conventions
  + avoid meaningless method names
+ 3 reasons for creating methods:
  + top-down decomposition of algorithms:
    + if you have a super long function, it's doing too many things. You should break it up!
    + put it into submethods that might even be part of a different class
    + lots of comments could mean further subdividing is necessary
  + avoid code duplication:
    + duplication makes maintenance difficult, error-prone
    + if you need it in multiple places, put it into a method that can be called wherever the code is needed
    + you could also inherit from a superclass to avoid duplication
  + deep nesting: 
    + excessive nesting of statements is one of the chief culprits of confusing code
    + more than 3 or 4 levels in and you've gone too far
    + creating additional sub-methods is the best way to remove this
+ put separate conditions on separate lines for booleans
+ put boolean expressions in parantheses
+ wrap lines intelligently
+ choose good variable names, not too long, not too short (except small iterators i,j,k or math stuff...)
+ Naming conventions:
  + UpperCamelCase, lowerCamelCase
  + snake_Case
  + first char of class is uppercase
  + first char of method/function/variable is lowercase
  + constants are ALL_CAPS with underscores
  + avoid "Dr. Seuss" naming (e.g. "thing1" and "thing2")
  + only abbreviate if you have to...
    + if you do, removing nonleading vowels (computer->cmptr) OR truncate (calculus->calc)
    + make sure it's pronouncable 
  
_Unit Testing_

The things we're building are so complicated, we need to test each little part of our code.

Write a little code, test it. Then write a little more, test it. Keep doing litle bit by bit. 

Test-driven development: write test cases FIRST, then build classes to pass the test cases.

Write a test driver which runs all of your small tests. 

Have an expected result, then compare what you got to the expected

_JUnit Testing Design_

IntelliJ has a builtin thing to automake a test for your class
```
import static org.junit.jupiter.api.Assertions.*

class StringOpsTest(){
    
    StringOps so;
    
    @Test
    void emptyString(){
        so = new StringOps();
        assertEquals("",so.reverse(""));
    }
    
    @Test
    void evenLengthString(){
        so = new StringOps();
        assertEquals("1234",so.reverse("4321"))
    }

}
```
JUnit provides method @BeforeEach so you don't have to keep redeclaring objects over and over
```
@BeforeEach
void setup(){
    so = new StringOps();
}
```

**7/19/24**
SQL Stuff

Each column in a SQL table declares the type that column may contain
+ Character strings:
  + CHARACTER(n) or CHAR(n) : fixed width n-character string, padded with spaces
  + CHARACTER VARYING(n) or VARCHAR(n) : variable-width string with max size of n characters
  + BIT(n) : an array of n bits
  + INTEGER, SMALLINT
  + FLOAT, REAL, DOUBLE PRECISION
  + NUMERIC(precision,scale) or DECIMAL(precision,scale)
  + BLOB : binary large object (images,sound,video,etc.)
  + CLOB : character large object (text documents)
  + DATE
  + TIME
  + TIME WITH TIME ZONE or TIMETZ : same as time but with timezone
  + TIMESTAMP : date and time together
  + TIMESTAMPTZ: date, time, timezone
  
+ Creating Tables:
```
create table if not exists book
{
    id integer not null primary key auto_increment  // auto_increment: fill in ID automatically
    title varchar(255) not null,                    // not null: signifies this can't be null
    category_id integer not null,
    genre varchar(32) not null,
    foreign key(genre) references genre(genre),     // declares that column genre points to column genre in table genre
    foreign key(category_id) references category(id)
};

drop table if exists book                           // deletion statement
```

+ Insertions
```
insert into book
(title, author,genre,category_id) values ('Oathbringer','Brandon Sanderson','Fantasy',3);
```
+ Updates
```
UPDATE member
SET name = 'Steve', email_address='steve@gmail.com'
WHERE id = 3 
```
+ Deletions
```
DELETE FROM table
WHERE condition

// (if you don't put a where clause, you delete the entire table)
```
+ Queries
```
SELECT Column, Column,...
FROM Table, Table,...
WHERE Condition
```
Examples:
```
SELECT *
FROM book    // selects the entire book table
```
```
SELECT author, title
FROM book
WHERE genre = "NonFiction" // selects author & title columns from book when they're nonfiction 
```
```
SELECT member.name, book.title
FROM member, books_read, book // this unions member x books_read x book
// to make this useful, we need to use a where clause
WHERE member.id = books_read.member_id AND book.id=books_read.book_id
```
```
SELECT member.name, book.title
FROM member
INNER JOIN books_read ON member.id = books_read.member_id
INNER JOIN book ON books_read.book_id = book.id
WHERE genre = "NonFiction" 
// this is the more modern way to do it
```
You can tell the database to not "commit" your changes immediately; you can make either all of it to happen, or non of it to.
This protects against SQL doing things only halfway.
```
BEGIN TRANSACTION; // e.g. do NOT make this permanent unless you can do it all
SQL statement 1;
SQL statement 2;
...
COMMIT TRANSACTION; or ROLLBACK TRANSACTION // this is how you commit changes (or undo everything)
```

JDBC: java's SQL API

+ load database driver: add MySQL driver for Java in project files
+ open a database connection
  + you want to open it, use it, close it
  + ```
    String connectionURL = "jdbc:mysql://localhost:3306/BookClub?"+;
    
    Connection connection = null;
    try(Connection c = DriverManager.getConnecton(connectionURL) {
       connection = c;
    
       // start transaction
       connection.setAutoCommit(false);
    }
    catch (SQLException ex){
    // ERROR
    }
``
+ start a transaction
+ execute queries and/or updates
  + ```
    List<Book> books = new ArrayList<>();
    
    String sql = "select id, title, author, genre, category_id from book"'
    
    try(PreparedStatement stmt = connection.prepareStatement(sql);
    ResultSet rs = stmt.executeQuery()) {
    
    while(rs.next()) {
        int id = rs.getInt(1);
        String title = rs.getString(2);
        String author = rs.getString(3);
        ...
        books.add(new Book(id, title, author,...);
    }
    
    }
    catch (SQLException ex) {
        // ERROR
    }
``
+ Execute an insert, delete, or update
  + ```
    String sql = "update book " +
    "set title = ?, author = ?, genre = ? ..."
    
    try(PreparedStatement stmt = connection.prepareStatement(sql)) {
        stmt.setString(1, book.getTitle());
        stmt.setString(2, book.getAuthor());
        ...
        if(stmt.executeUpdate()==1) {
            System.out.println("updated book " + book.getID());
        }
        else {
            System.out.println("Failed to update book " + book.getID());
        }  
    }
    catch(SQLException ex){
        // ERROR
    }
``
+ Commit or rollback a transaction
  + connection.commit();
  + or 
  + catch(SQLException ex) {
  + if (connection!=null) {
  + connection.rollback();
  + throw ex

Don't put username/password in source code! Can read from a gitignored config file instead

Create a user with permission to access the database:

CREATE USER 'smblodgett'@'localhost' IDENTIFIED BY 'mypassword';
GRANT ALL on BookClub.* to 'smblodgett'@'localhost';


