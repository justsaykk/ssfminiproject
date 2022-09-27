# Server Side Foundation (VTTP) mini project

## App Introduction
1. This application is a "bookmark" app. Users will be able to find a cocktail of their choice and add it to their profile.
2. The API used for this application is [CocktailDB](https://www.thecocktaildb.com/)

### Drinks Page
1. The search function is a search via ingredient. It will return a list of drinks that has the search variable as an ingredient.
    - If the API returns null, an empty page will show with a message and a search box.
2. Clicking "View Details" will bring users to a more in-depth page of the drink.
3. The default value is set to "Scotch".
4. Users will be able to find drinks based on the drink name.

### Detailed Drinks Page
1. In the detailed drinks page, users will be able to see the ingredent list and the preparation instructions of the drink.
2. Users will also be able to add the drink to their profile.
3. If the user is not logged in, the "Add to profile" button will not appear.
4. If the drink has already been added before (Duplicated), an error message will prompt.
5. If the drink has been successfully added, a success message will prompt.

### Profile Page
1. This is a page where users will be able to view the drinks that was added to their profile.
2. Users can view the drink details.
3. Users can delete the drink from their profile.
4. Users can edit their profile details.
5. Users will be able to see their profile picture, name, email and country of residence.
6. If users have no drinks in their profile, a button to browse drinks will be in place.
7. Users will be able to delete their profile.

### Edit Profile Page
1. The previous information has been autofilled.
2. Name & Email cannot be edited.
3. Upon submission:
    - Hidden fields will capture old values.
    - The service will compare old and new values from individual fields.
    - The service will update if old value != new value.

### Login Page
1. Login can only be done via Github or Google
2. With this OAuth2 implementation, the profile creation page & the old login page has been obsoleted.
3. Users do not need to key in any details and profiles will be retrieved automatically.

### Error Handling
1. 404 Errors have its own cute ghostly page (credits to [Diogo Gomes](https://codepen.io/diogo_ml_gomes/pen/PyWdLb)).
2. As much as possible, no internal server error (500) should occur.



## Project Requirements

### Springboot

| Requirement | Progress    |
| ----------- | ----------- |
| Must handle POST & GET request | Done |
| Must include @PathVariable | Done |
| Must include both @Controller & @RestController | Done |
| POST must consume either form or JSON data | Done |
| Must support more than 1 user | Done |
| Must include a min. of 3 views | Done |

### RESTful API

| Requirement | Progress    |
| ----------- | ----------- |
| Must make request to external API | Done |
| Must use a new API | Done |

### HTML & CSS

| Requirement | Progress    |
| ----------- | ----------- |
| Bootstrap v5.x | Done |

### Database

| Requirement | Progress    |
| ----------- | ----------- |
| Use redis database | Done |

### Release Schedule

At least once a week

| Requirement | Progress    |
| ----------- | ----------- |
| Aug 15-19   | v1.0-aplha.1|
| Aug 22-26   | v1.0-aplha.2|
| Aug 29-02   | v1.0.3      |
| Sep 05-09   | No releases |
| Sep 12-16   | No releases |
| Sep 19-23   | v2.0.0      |