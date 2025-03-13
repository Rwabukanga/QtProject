Authentication
User Registration
POST /auth/register
Registers a new user.


User Login

POST /auth/login

Authenticates a user and returns a JWT token.

User Logout

POST /auth/logout

Invalidates the user's token.

URL Shortening

Shorten URL

POST /shorten

Creates a shortened URL (Requires authentication).

Get User URLs

GET /urls

Retrieves all URLs created by the authenticated user.

URL Analytics

GET /analytics/:shortUrl

