# Iteration 3 Architecture Diagram

- This diagram provides an overview of our updated architecture diagram.
- It illustrates the core architecture of our application, organized into 3 distinct layers:
  - Presentation Layer: Manages UI components (Activities and Fragments).
  - Logic Layer: Implements business rules through Services and Validators.
  - Persistence Layer: Handles data storage and retrieval operations.
- Domain Objects represent the core data models used throughout all layers.


```mermaid
graph LR
    %% Domain Objects
    subgraph DomainObjects["Domain Specific Objects"]
    style DomainObjects fill:#D1E8FF,stroke:#B1C9E3,stroke-width:2px
        direction TB
        User(["User"])
        style User fill:#D0F1EB,stroke:#86B7B3,stroke-width:2px
        Entry(["Entry"])
        style Entry fill:#D0F1EB,stroke:#86B7B3,stroke-width:2px
    end
    
    %% Presentation Layer
    subgraph PresentationLayer["Presentation Layer"]
        style PresentationLayer fill:#D1E8FF,stroke:#B1C9E3,stroke-width:2px
        
        subgraph Activities["Activities"]
            style Activities fill:white,stroke:#B1C9E3,stroke-width:2px
            direction LR
            MainActivity(["MainActivity"])
            style MainActivity fill:#D0F1EB,stroke:#86B7B3,stroke-width:2px
            LoginActivity(["LoginActivity"])
            style LoginActivity fill:#D0F1EB,stroke:#86B7B3,stroke-width:2px
            SignUpActivity(["SignUpActivity"])
            style SignUpActivity fill:#D0F1EB,stroke:#86B7B3,stroke-width:2px
            FinanceActivity(["FinanceActivity"])
            style FinanceActivity fill:#D0F1EB,stroke:#86B7B3,stroke-width:2px
        end
        
        subgraph Fragments["Fragments"]
            style Fragments fill:white,stroke:#B1C9E3,stroke-width:2px
            direction LR
            HomeFragment(["HomeFragment"])
            style HomeFragment fill:#D0F1EB,stroke:#86B7B3,stroke-width:2px
            AnalyticsFragment(["AnalyticsFragment"])
            style AnalyticsFragment fill:#D0F1EB,stroke:#86B7B3,stroke-width:2px
            TrackingFragment(["TrackingFragment"])
            style TrackingFragment fill:#D0F1EB,stroke:#86B7B3,stroke-width:2px
            ProfileFragment(["ProfileFragment"])
            style ProfileFragment fill:#D0F1EB,stroke:#86B7B3,stroke-width:2px
        end
    end
    
    %% Logic Layer
    subgraph LogicLayer["Logic Layer"]
        style LogicLayer fill:#D1E8FF,stroke:#B1C9E3,stroke-width:2px
        
        subgraph EntryLogic["Entry Logic"]
            style EntryLogic fill:white,stroke:#B1C9E3,stroke-width:2px
            direction TB
            EntryServices(["EntryServices"])
            style EntryServices fill:#D0F1EB,stroke:#86B7B3,stroke-width:2px
            EntryValidator(["EntryValidator"])
            style EntryValidator fill:#D0F1EB,stroke:#86B7B3,stroke-width:2px
        end
        
        subgraph UserLogic["User Logic"]
            style UserLogic fill:white,stroke:#B1C9E3,stroke-width:2px
            direction TB
            UserServices(["UserServices"])
            style UserServices fill:#D0F1EB,stroke:#86B7B3,stroke-width:2px
            UserValidator(["UserValidator"])
            style UserValidator fill:#D0F1EB,stroke:#86B7B3,stroke-width:2px
        end
    end
    
    %% Persistence Layer
    subgraph PersistenceLayer["Persistence Layer"]
        style PersistenceLayer fill:#D1E8FF,stroke:#B1C9E3,stroke-width:2px
        
        subgraph EntryPersistenceGroup["Entry Persistence"]
            style EntryPersistenceGroup fill:white,stroke:#B1C9E3,stroke-width:2px
            direction TB
            EntryPersistence(["EntryPersistence"])
            style EntryPersistence fill:#D0F1EB,stroke:#86B7B3,stroke-width:2px
            EntryPersistenceStub(["EntryPersistenceStub"])
            style EntryPersistenceStub fill:#D0F1EB,stroke:#86B7B3,stroke-width:2px
        end
        
        subgraph UserPersistenceGroup["User Persistence"]
            style UserPersistenceGroup fill:white,stroke:#B1C9E3,stroke-width:2px
            direction TB
            UserPersistence(["UserPersistence"])
            style UserPersistence fill:#D0F1EB,stroke:#86B7B3,stroke-width:2px
            UserPersistenceStub(["UserPersistenceStub"])
            style UserPersistenceStub fill:#D0F1EB,stroke:#86B7B3,stroke-width:2px
        end
    end

    %% Connections
    MainActivity --> LoginActivity
    MainActivity --> SignUpActivity
    LoginActivity --> FinanceActivity
    SignUpActivity --> FinanceActivity
    
    FinanceActivity --> EntryLogic
    FinanceActivity --> UserLogic
    FinanceActivity --> Fragments
    
    EntryServices --> EntryValidator
    UserServices --> UserValidator
    
    EntryLogic --> EntryPersistenceGroup
    UserLogic --> UserPersistenceGroup
    
    EntryPersistence --> EntryPersistenceStub
    UserPersistence --> UserPersistenceStub
    
    %% Make arrows lighter
    linkStyle default stroke:#666666,stroke-width:1.5