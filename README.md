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

### Database
The database has a few *key* keys:
1. registeredprofiles
2. registerednames
3. profilemap (see [here](#database-db-learning))
4. user's email
5. user's name


```javascript
// registeredprofiles & registerednames looks like this:
registeredprofile = ["user01@user.com", "user02@user.com", "user03@user.com"]
registeredname = ["user01", "user02", "user03"]
/* They serve as a quick way to check if a user has been registered in the database */

// user's email looks like this:
user01@user.com = {
    name: "user01",
    profilePic: "https://someurl.com.jpg",
    country: "Singapore"
}

// user's name looks like this:
user01 = ["11121", "12345", "56789"] // <-- stores drink ids
```

user's `email` and `name` are expected to keep duplicating as more and more users utilize the app. However, the other 3 keys will only expand within.

## Project Requirements

| Requirements                                        |            Status            |
| --------------------------------------------------- | :--------------------------: |
| Must handle `POST` & `GET` request                  | :white_check_mark: Completed |
| Must include `@PathVariable`                        | :white_check_mark: Completed |
| Must include both `@Controller` & `@RestController` | :white_check_mark: Completed |
| POST must consume either form or `JSON` data        | :white_check_mark: Completed |
| Must support more than 1 user                       | :white_check_mark: Completed |
| Must include a min. of 3 views                      | :white_check_mark: Completed |
| Must make request to external API                   | :white_check_mark: Completed |
| Must use a new API                                  | :white_check_mark: Completed |
| Bootstrap v5.x                                      | :white_check_mark: Completed |
| Use redis database                                  | :white_check_mark: Completed |

## Difficulties and Learnings

#### Database {#db-learning}
The use of redis as a persistent storage meant that only simple key-value pairs can be stored. The method of storage, therefore, influenced heavily on what could or could not be done.
This application requires a more SQL-like storage mechanism. Therefore, a key-to-key map was needed. 
The map is called `profilemap` and is a map linking the user's email to its name and would look something like this:

```javascript
profilemap = {
    "user01": "user01@user.com",
    "user02": "user02@user.com"
}
```

This allows for services to have a fixed *"table"* to reference from. It also prevents the database from having individual keys floating around. 


#### @RestController
Unlike an application with separate front and back-end components, this application is a single, app that encompases both front and back end stuff. 
This brings forth an interesting scenario whereby the `@RestController` is not strictly needed as the typical `@Controller` would have access to the same resources as the *"backend"* `@RestController` would have. 
Therefore, most of the routes from `@RestController` redirects to a view. 

The way the redirection works is through the use of `HttpServletResponse response` and a `response.sendRedirect()` to form something like this:

```java
    @PostMapping(path = "/route")
    public void route(HttpServletResponse response) throws IOException {
        // Some code here...
        response.sendRedirect("/another_route");
    }
```

Even though all of the `@RestController` routes look like this, I recognize that normally, the `@RestController` would return a `JSON` format response.

#### OAuth2 login
From the initial release to v2.0.0, the application had a cumbersome workflow to store user's profile and saved cocktails. 

Assuming the user's profile has been created, users would need to:
1. Input name and save drink to profile (one drink at a time)
2. Go to *login* page and input name

Therefore, if a user wanted to save 5 drinks, the user would need 5 name inputs and 5 clicks on every input.

By delegating the login to Google and/or Github, users would only need to login once and save on all the name inputs to save a drink to the profile. Furthermore, the profiles are automatically created upon accessing the profile page. In addition, I did not need to use `HttpSession` as a method to store user data when traversing the app. 

##### Problems:
By default, *OAuth2* would only authenticate non-state-changing requests (i.e. only "GET"), having multiple forms that utilize `method = "POST"` would prompt a `403 Forbidden` error. 

To circumvent this, I needed to disable cross-site-request-forgery (csrf) setting. By doing so, I'm compromising the safety of the application. However, the risk is low as the app does not save any confidential information about the user. 