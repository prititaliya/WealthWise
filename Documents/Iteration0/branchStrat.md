# Branching Strategy (Git-flow)


## Structure:
### Master branch
Contains the most current released code
### Develop branch
Contains code from latest development changes for the next release
### Feature branch
Originating from the develop branch
Code for each work unit (dev task/user story)
Merged back into the Develop branch after completion


## Procedure:
1. Create Develop branch at the start of each new iteration
..* Cloned from Master branch
2. For each feature, create a Feature branch
..* Cloned from Develop branch
..* Format: feat#[ISSUE #]_[FEATURE TITLE]
3. For each user story from a feature, create a User Story branch
..* Cloned from the associated Feature branch
..* Format: story#[ISSUE #]_[USER STORY TITLE]
4. For each dev task from a user story, create a Dev Task branch
..* Cloned from the associated User Story branch
..* Format: devTask#[USER STORY ISSUE #]_[SHORT DESCRIPTION/TITLE]
..* Should consist of *minimal* code.


## Branch Naming Scheme (Examples)
..* bug#3744_fixCancelMessaging
..* story#12_makeSearchRecipes
..* dev#14_techDebt
..* story#12_WIP_searchBarStyling (WIP = work in progress)
