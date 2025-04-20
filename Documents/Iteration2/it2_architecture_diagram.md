# Iteration 2 Architecture Diagram

- This diagram provides an overview of our updated architecture diagram.


```mermaid

graph LR
    direction TB
    subgraph PresentationLayer["Presentation Layer"]
        style PresentationLayer fill:#D1E8FF,stroke:#B1C9E3,stroke-width:2px
        MainActivity[MainActivity]
        style MainActivity fill:#D0F1EB,stroke:#86B7B3,stroke-width:2px
        subgraph PresentationChildren["Presentation Activities"]
            direction TB
            LoginActivity[LoginActivity]
            style LoginActivity fill:#D0F1EB,stroke:#86B7B3,stroke-width:2px
            SignUpActivity[SignUpActivity]
            style SignUpActivity fill:#D0F1EB,stroke:#86B7B3,stroke-width:2px
            FinanceActivity[FinanceActivity]
            style FinanceActivity fill:#D0F1EB,stroke:#86B7B3,stroke-width:2px
        end
    end
    subgraph LogicLayer["Logic Layer"]
        style LogicLayer fill:#D1E8FF,stroke:#B1C9E3,stroke-width:2px
        subgraph EntryLogic["Entry Logic"]
            direction TB
            EntryServices[EntryServices]
            style EntryServices fill:#D0F1EB,stroke:#86B7B3,stroke-width:2px
            EntryValidator[EntryValidator]
            style EntryValidator fill:#D0F1EB,stroke:#86B7B3,stroke-width:2px
        end
        subgraph UserLogic["User Logic"]
            direction TB
            UserServices[UserServices]
            style UserServices fill:#D0F1EB,stroke:#86B7B3,stroke-width:2px
            UserValidator[UserValidator]
            style UserValidator fill:#D0F1EB,stroke:#86B7B3,stroke-width:2px
        end
    end
    subgraph PersistenceLayer["Persistence Layer"]
        style PersistenceLayer fill:#D1E8FF,stroke:#B1C9E3,stroke-width:2px
        subgraph EntryPersistenceGroup["Entry Persistence"]
            direction TB
            EntryPersistence[EntryPersistence]
            style EntryPersistence fill:#D0F1EB,stroke:#86B7B3,stroke-width:2px
            EntryPersistenceStub[EntryPersistenceStub]
            style EntryPersistenceStub fill:#D0F1EB,stroke:#86B7B3,stroke-width:2px
        end
        subgraph UserPersistenceGroup["User Persistence"]
            direction TB
            UserPersistence[UserPersistence]
            style UserPersistence fill:#D0F1EB,stroke:#86B7B3,stroke-width:2px
            UserPersistenceStub[UserPersistenceStub]
            style UserPersistenceStub fill:#D0F1EB,stroke:#86B7B3,stroke-width:2px
        end
    end
    subgraph DomainObjects["Domain Specific Objects"]
        style DomainObjects fill:#D1E8FF,stroke:#B1C9E3,stroke-width:2px
        User[User]
        style User fill:#D0F1EB,stroke:#86B7B3,stroke-width:2px
        Entry[Entry]
        style Entry fill:#D0F1EB,stroke:#86B7B3,stroke-width:2px
    end
    MainActivity --> LoginActivity
    MainActivity --> SignUpActivity
    LoginActivity --> FinanceActivity
    SignUpActivity --> FinanceActivity
    FinanceActivity --> EntryLogic
    FinanceActivity --> UserLogic
    EntryServices --> EntryValidator
    UserServices --> UserValidator
    EntryPersistence --> EntryPersistenceStub
    UserPersistence --> UserPersistenceStub
    UserLogic --> UserPersistenceGroup
    EntryLogic --> EntryPersistenceGroup
