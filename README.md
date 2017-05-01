## Server

Install dependencies

```
gem install sinatra
gem install thin
```

And run: `ruby server.rb -o 0.0.0.0`

To introduce an N second delay before server response, add the 'slow' environment
variable to the run. For instance `slow=10 ruby server.rb -o 0.0.0.0`

To make the server return 500 errors on every post, add `post_error=1` to the environment

## Unit tests

Configure Android Studio to run local unit tests per http://developer.android.com/training/testing/start/index.html#build

## Android client

Add a local.properties file to the android/app/src/main/assets/ directory:
```
## Local properties. Do not save these to source control
serverUrl=http://10.0.1.6:4321
httpTimeoutSeconds=10
```
Where serverUrl is the IP of your box, if you are running the local server
distributed with this project.

