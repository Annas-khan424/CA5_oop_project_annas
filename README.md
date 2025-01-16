# JAVA_PROJECT

This project was a part of my second-year project. In this project I have used created a database which includes the data of staff members working in the bar. It is connected to SQL and has a server as well as a client field.

1. The data is stored in Array List form. A HashMap is created to populated using appropriate keys.

2. A menu is created where the key provided the user is used to display appropriate message.

3. Created a TreeMap using different key as one used in HashMap and displaying object from TreeMap.

4. Creating PriorityQueue and
> adding two third-priority elements
> 
> adding two second-priority level items
> 
> removing and display one element
> 
> adding one top-priority element
> 
> remove and display all elements in priority order
> 

5. Provide a menu option to return and display all the objects in priority order of a selected integer field within a selected string field.The time complexity is also mentioned in the text file maned feature 8.

6. Create and populate a MySQL Database Table to store the details of the staff members.Create a corresponding Data Access Layer with a DTO, a DAO and corresponding Interfaces that allow access to your database table.


7. Database menu provides following features:
>
>Display all staff
>
>Display staff by ID
>
>Delete Staff Details by key
>
>Inserting new staff members
>
>Listing staff members using filters
>
>Retrieving all staff details as JSON format
>
>Finding staff by ID in JSON format
>
8. Implement a client-side menu item that allows a user to select the option “Display Entity by ID” where the Entity is your selected entity class. The client will send a request (command) to the server, along with the user inputted identifier (ID), in accordance with your specified protocol. The server will process the request, use an appropriate DAO to access the entity from the database, convert the entity into JSON representation, and return the JSON via a socket to the client. The client will receive the JSON data, parse the data and instantiate and populate an entity object with the data. The data will then be retrieved from the entity object and displayed, on the client screen in a formatted manner.
9. Implement a client-side menu option “Display all Entities” that will send a request to the server to obtain a list of all entities. The server will process the request (command) and will use a DAO to retrieve all entities, convert to JSON format, and return the JSON data to the client. The client will parse the JSON data and use it populate a list of entities. All entities will be displayed and neatly formatted, from the list of entity objects.
10. Implement a client-side menu item that will allow the user to input data for an entity, serialize the data into JSON formatted request and send the JSON request to the server. The server will add the entity to the database using a relevant DAO, and will send a response to the client. The response will return the Entity object data incorporating the newly allocated ID (if the ID was auto generated). The client will display the newly added entity, along with its auto generated ID. If the insert fails, an appropriate error should be displayed.
11. In a manner similar to above, provide a menu item that will delete an entity by ID, send a command to the server to undertake the delete, and display an appropriate message on the client.
12. Detail all of the messages that pass between the client and server to implement all of the features of this CA. Draw this out in a diagram showing all of the data that is passed in each direction in the appropriate format
