##Factory 
Replace using a class constructor, type of object created can be determined at runtime. 
>Application: command runner based on the command 

##Prototype
Reduces the cost of object initialization by cloning existing object. Good for creation
of object that is pretty similar most of the time. Allows for creating of object
dynamically when needed. 
>Application: N/A

##Singleton
Allows for creation of one object only. Global information and prevents overwriting 
the object.
>Application: Session information, Memory

##Bridge
Decouple abstractions from its implementation using an interface. Allows for classes
that vary independently of each other. 
>Application: Feedback with different implementation 

##Decorator
Class that wraps extra functionality at runtime based on conditions.
>Application: edit status or feedback based on conditions 

##Facade 
Helps to simplify complex operations that requires multiple classes 
>Application: Logic class 

##Command
Parameterize object depends on request. Invoke appropriate in Receiver. 
>Application: CommandRunner 

##Mediator
Decouple further two classes that often talks with each other 
>Application: StorageHandler handles Logic and Memory

##Observer
Any changes to the object will trigger an action by the listener 
