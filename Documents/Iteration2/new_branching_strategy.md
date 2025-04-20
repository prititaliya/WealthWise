# Branching Strategy

## Structure:

### Main Branch
- Contains the most stable and current production-ready code.

### Develop Branch
- Receives merges from feature branches.
- Provides a staging area for code before moving to main.

### Feature Branches
- These are created from the `Develop` branch.
- They represent a new feature for the app.
- They are merged back into `Develop` after completion and approval.

### User Story Branches
- These are created from the associated `Feature` branch.
- They represent a specific user story under the feature.
- They get merged back into the corresponding `Feature` branch upon completion.
- They have their own `Dev` and `Test` branches.
- They sometimes have `Bug` branches if necessary.

### Dev Task Branches
- These are created from the associated `User Story` branch.
- They represent a specific development task within a user story.
- Ideally they should contain minimal, focused changes. Few files affected as possible.
- They get merged back into the `User Story` branch once completed.

### Testing Branches
- These are created from either a `User story` or `Feature` branch.
- They are used for testing and debugging purposes.
- Later they are merged back into the corresponding parent branch once testing is complete.

### Technical Debt Branches
- Refactoring tasks created from the `Develop` branch.
- Identified after iteration feedback or during development, and scheduled for later when needed.
- Focus on code quality, performance, or architectural improvements.

## Procedure:

1. **Create a Develop Branch**
   - Cloned from `main`.
   - Naming convention: `Develop`

2. **Create a Feature Branch**
   - Cloned from `Develop`.
   - Naming convention: `Feature#[NUMBER]_[SHORT DESCRIPTION]`

3. **For each user story, create a User Story Branch**
   - Cloned from the associated `Feature` branch.
   - Naming convention: `Story#[NUMBER]_[SHORT DESCRIPTION]`

4. **For each dev task, create a Dev Task Branch**
   - Cloned from the associated `User Story` branch.
   - Naming convention: `Dev#[NUMBER]_[SHORT DESCRIPTION]`

5. **For testing purposes, create a Testing Branch**
   - Cloned from either a `Feature` or `User Story` branch.
   - Naming convention: `Test#[NUMBER]_[SHORT DESCRIPTION]`

6. **If needed, create a Bug Branch**
   - Cloned from either a `Feature` or `User Story` branch.
   - Naming convention: `Bug#[NUMBER]_[SHORT DESCRIPTION]`

7. **If needed, after evaluations or when availability allows, create a TechDebt Branch**
   - Cloned from a `Develop` branch.
   - Naming convention: `TechDebt#[NUMBER]_[SHORT DESCRIPTION]`

## Branch Naming Scheme (Examples):
- `Feature#009_AccountManagement`
- `Story#034_UserAuthentication`
- `Dev#025_CreateLoginActivity`
- `Test#008_LoginTesting`


# Git Branching Strategy

```mermaid
%%{init: { 'theme': 'neutral', 'themeVariables': { 'fontSize': '16px', 'fontFamily': 'arial' } } }%%
graph TD
  %% Add blank node for spacing
   title[Git Branching Strategy]
   style title fill:none,stroke:none
   title --> main
   
   main --> develop
   develop --> Feature#001_FeatureName
   develop --> TechDebt#001_TechDebtName
   
   Feature#001_FeatureName --> Story#001_StoryName
   Feature#001_FeatureName --> Story#002_StoryName
   
   Story#001_StoryName --> Dev#001_DevName
   Story#001_StoryName --> Dev#002_DevName
   Story#001_StoryName --> Test#001_TestName
   Story#001_StoryName --> Bug#001_BugName
   
   Story#002_StoryName --> Dev#003_DevName
   Story#002_StoryName --> Dev#004_DevName
   Story#002_StoryName --> Test#002_TestName
   Story#002_StoryName --> Bug#002_BugName
   
   classDef main fill:#333,stroke:#333,stroke-width:4px,color:white;
   classDef develop fill:#cc84f4,stroke:#cc84f4,stroke-width:2px,color:black;
   classDef feature fill:#fffae1,stroke:#fffae1,stroke-width:2px,color:black;
   classDef story fill:#81f1f7,stroke:#81f1f7,stroke-width:2px,color:black;
   classDef dev fill:#9dffb0,stroke:#9dffb0,stroke-width:2px,color:black;
   classDef test fill:#fff563,stroke:#fff563,stroke-width:2px,color:black;
   classDef bug fill:#c48d3f,stroke:#c48d3f,stroke-width:2px,color:black;
   classDef techDebt fill:#Fb78c1,stroke:#fb78c1,stroke-width:2px,color:black;
   classDef titleClass fill:none,stroke:none,color:#333,font-size:20px,font-weight:bold;
   
   class main main;
   class develop develop;
   class Feature#001_FeatureName,Feature#002_FeatureName feature;
   class Story#001_StoryName,Story#002_StoryName story;
   class Dev#001_DevName,Dev#002_DevName,Dev#003_DevName,Dev#004_DevName dev;
   class Test#001_TestName,Test#002_TestName test;
   class Bug#001_BugName,Bug#002_BugName bug;
   class TechDebt#001_TechDebtName techDebt;
   class title titleClass;
