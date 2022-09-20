# Server Side Foundation (VTTP) mini project

## App Introduction
1. This application is a "bookmark" app. Users will be able to find a cocktail of their choice and add it to their profile.
2. The API used for this application is [CocktailDB](https://www.thecocktaildb.com/)

### Drinks Page
1. The search function is a search via ingredient. It will return a list of drinks that has the search variable as an ingredient.
    - If the API returns null, an empty page will show with a message and a search box.
2. Clicking "View Details" will bring users to a more in-depth page of the drink.
3. The default value is set to "Scotch".

### Detailed Drinks Page
1. In the detailed drinks page, users will be able to see the ingredent list and the preparation instructions of the drink.
2. Users will also be able to add the drink to their profile.
3. If the profile has not be set-up, clicking "add to profile" will send users to the create profile page

### Create profile Page 
1. There are 2 versions of this page. If users are redirected here due to the addition of a drink to an uninitiated profile, the name will be greyed out.
2. Users will be able to add their email, country of residence and their profile picture url.
3. If the supplied picture url is not a valid url, a placeholder image will be inserted.
4. Upon submission:
    - A User profile will be created
    - The email will be added to the key: "registeredprofile"
    - The name will be added to the key: "registerednames"
    - The profilemap will be updated
    - The email will be used as a key to store all the other information
    - Users will be redirected to their profile page

### Profile Page
1. This is a page where users will be able to view the drinks that was added to their profile
2. Users can view the drink details
3. Users can delete the drink from their profile
4. Users can edit their profile details
5. Users will be able to see their profile picture, name, email and country of residence
6. If users have no drinks in their profile, a button to browse drinks will be in place

### Edit Profile Page
1. The previous information has been autofilled
2. Only the name cannot be edited
3. Upon submission:
    - Hidden fields will capture old values
    - The service will compare old and new values from individual fields
    - The service will update if old value != new value

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
| Aug 15-19   | v1.0-aplha.1 released|
| Aug 22-26   | v1.0-aplha.2 released|
| Aug 29 - Sep 02   | v1.0.3            |
| Sep 05-09   |             |
| Sep 12-16   |             |
| Sep 19-23   |             |