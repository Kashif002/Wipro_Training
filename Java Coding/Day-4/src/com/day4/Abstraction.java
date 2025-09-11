Encapsulation (data hiding by wrapping the data members & member functions) & Abstraction (code/ Business Logic -  implementation hiding) --- Hiding 


Abstraction -- Abstract & Interface

Hiding the implementation and showing the necessary/relevant information -- What is an object 

Abstract classes are not 100% abstraction  --- we are implementing using abstract keyword : In abstract class we have both abstract methods as well as 
non abstract method --  where implementation/definition of abstract methods will be given by child class while overriding the abstract methods which is mandatory to override the abstract methods
and overriding is not mandatory for non abstract methods
//Amazon                                                                    Zara (.net/java (api for paymentProcessing))
abstract class Payment{
            
           private int years = 2;
           
           public int getYears()
           {
           return years;
           }
                                                                               class ProductPayment extends Payment
//abstract method  --  (methods declared without an implementation)              
abstract void paymentProcessing() ;                                            {  // Zara has to give the implementation or definition of paymentProcessing method because it is abstract   }

   // api -- call  (hidden implementation)

//non -abstract method
void discount()
{
// we can give the definition
 
}

}


interface --- (100% abstraction) --we are implementing using interface keyword  where all methods are by default abstract  (but before java 1.8) because after 1.8 onvwards we do have static or default methods for extension( as in functional interface)-- 

It's an agreement/contract where the implementation of all methods of an interface will be given by a child class and by default the variables in an interface are final
Interface is used to achieve 100% abstraction  
Swiggy																		PizzaHut
interface Process                                                     class Deliver implements Process
{                                                                        {// override all the three methods that are mandatory and give the definition}

// by default it is abstract
 void paymentProcessing() ;

 
 void productListing();

 void productPrice();


}


interface CrudOperations             class AdminUser implements CrudOperations
{										{

void add();								void add(){}
															
void delete();							void delete() {}
void update();							void update(){}
void view();								void view(){}
}										}						

class Hr implements CrudOperations
{										{

							void add(){}														
							void delete() {}
							void update(){}
							void view(){}
}
}



