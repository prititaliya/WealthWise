# Iteration 2 Worksheet
**COMP 3350: Software Engineering**  
**Group 11 - Code-kages**

## Paying Off Technical Debt

### Removing Duplication from UserService Class
We identified that we were doing validation for both login and signup functions in the same file, which lead to a lot of code duplication. Instead, we decided to dedicate a separate file, named `UserValidator`, to validate all user information.  
This revised version looks cleaner, is easier to understand, and it also demonstrates better Open/Closed principles. For example, if we wanted to create a new set of rules for email validation in our older version of code, we would have to modify the `UserService` class. However, with the newer version we simply just extend the `UserValidator` class with the new set of rules.

- **Debt Classification**: Code Debt  
    - The older version violated the DRY design principle and increased the maintenance complexity. The updated version centralized the validation in one class, which can be used by other related classes in the future, if needed. This demonstrates good DRY and Open/Closed principles.  
- [Link to commit](https://code.cs.umanitoba.ca/comp3350-winter2025/a01-g11-code-kages/-/commit/28ddb17214386a3249d32879ef56a779ca611659#ebe2059b847e74b4cafc458239d3cc97f5a78418_88_93)  
    - `checkEmail()` and `checkPassword()` were removed and handled by the `UserValidator.java` class

---

### Try/Catches, Not Logic, and Returns on Voids
Within the presentation layer, we identified code that belonged in the logic layer, as well as several return on void statements. Instead, we should have been using try/catch blocks in the presentation layer, accompanied by exception throws in the logic layer, which we tried to implement in all possible fragments in the logic layer.  
We implemented revised code with try/catch blocks in all fragments to avoid logic within the presentation layer. We also made sure that appropriate custom exceptions are thrown from the logic layer to help both users and developers.

- **Debt Classification**: Code Debt  
    - Previously, the logic and presentation layers were tightly coupled, which increased debugging complexity. After the appropriate code was allocated to the logic layer, the presentation layer now only handles user interaction. Separation of concerns improved scalability and reduced chances of technical debt in the future.  
- [Link to commit](https://code.cs.umanitoba.ca/comp3350-winter2025/a01-g11-code-kages/-/commit/5a5791c56acd0cb6e4ad45e44e68423328b350d6#e726b0ab9ab64b5bb1e36e12524843956d7d9439_66_55)  
    - All if/else blocks from login and signup fragments were replaced by try/catch blocks. We followed the same standards for all other future fragments.

---

## SOLID Violation (G12 - ShortnSWEet)
We found a Single Responsibility Principle violation in Group 12's `LoggingEvents.java` file. It currently handles event logic and several validation purposes. A dedicated validator class would make the code more modular and demonstrate better SOLID principles.  
- [Link to Issue](https://code.cs.umanitoba.ca/comp3350-winter2025/a01-g12-shortnsweet/-/issues/74)

---

## Retrospective
In our group retrospective at the end of Iteration 1, one of the things we wanted to focus on was prioritizing branch management for the next iteration. Although we had organized and planned a branching strategy to use throughout Iteration 1, it was rarely used, which lead to several instances where we ran into conflicts directly involving our disorganized branching structure.  
We were able to incorporate better branching strategies in Iteration 2, which resulted in significantly fewer branching conflicts. We were able to allocate less time toward debugging and handling conflicts, and more time toward quality coding.  
- [Here is a link to an example branch breakdown from Iteration 2](https://code.cs.umanitoba.ca/comp3350-winter2025/a01-g11-code-kages/-/blob/main/Documents/Iteration2/group11_branching_example.pdf?ref_type=heads)

---

## Design Patterns
### Singleton Pattern
The Singleton Pattern is applied in our `Services` class to make sure only one instance of `EntryPersistence` and `UserPersistence` is used throughout the session. This guarantees centralized management of these objects, which should be unique across the system. Using the Singleton Pattern for managing database connections helps maintain a single controlled connection throughout the application lifecycle.  

Our implementation of the Singleton pattern in the [Services class here](https://code.cs.umanitoba.ca/comp3350-winter2025/a01-g11-code-kages/-/blob/main/app/src/main/java/comp3350/wwsys/application/Services.java?ref_type=heads).  
The Singleton Pattern can be found in more detail here: [GeeksforGeeks Singleton Design Pattern](https://www.geeksforgeeks.org/singleton-design-pattern/).

---

## Iteration 1 Feedback Fixes
[Link to GitLab issue](https://code.cs.umanitoba.ca/comp3350-winter2025/a01-g11-code-kages/-/work_items/123)  
This was one of the GitLab issues we created based off of the feedback from our Iteration 1 submission. There were several problems with our object structure at the end of Iteration 1:
- Our object structure was overly complicated
- Contained duplicated code
- Used hardcoded strings for categorization
- Had code that belonged in the logic layer
- Threw exceptions (objects should never throw exceptions)

To fix these issues, we completely restructured our object class structure, with a focus on these key changes:
- Condensed all related object files to just one file, `Entry.java` (formerly 5 separate files)  
    - [Link to commit](https://code.cs.umanitoba.ca/comp3350-winter2025/a01-g11-code-kages/-/commit/f45e44d5fabe03c52e96d8b59714104cbc3fcb36)
- Expense and Income category enums were created and placed in their own files to replace hardcoded strings  
    - [Link to commit](https://code.cs.umanitoba.ca/comp3350-winter2025/a01-g11-code-kages/-/commit/3dcb532be7a06dbf57b06c9f199960f20697e591)
- Moved all necessary validation logic to a dedicated validator class within the logic layer  
    - [Link to commit](https://code.cs.umanitoba.ca/comp3350-winter2025/a01-g11-code-kages/-/commit/cce01f190646ff2e266219d71dc053d2feb344d7)
