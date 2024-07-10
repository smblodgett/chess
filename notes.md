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
   2. You should try and use design and building in a synergistic loop.
2. Abstraction! 
   1. Our job is to design a new language, in a way, that raises the level of abstraction to the thing you're working on. Rather than representing, say, DNA with string classes, you should create a DNA class that fits what DNA is better.
   2. You should be able to do something without understanding how it works internally.
   3. Since classes are usually complex, real-world things, you must be wise about what to include and what to leave out.
3. Good naming!
4. Single-responsibility principle!
   1. each function/variable should only do one thing!