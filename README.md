# Craigslist Watcher

## Introduction
You want to find a new apartment, but new Craigslist listings get snapped up as soon as they are posted! 

Craigslist Watcher is a Java web application that allows users to track new results for a specified Craigslist search. Once set up, the search will run periodically, and users will be emailed when new results are found. 

The index page includes an input field where the user can paste the URL for a Craigslist search. Upon submitting, the user is taken to a page where they enter their email, name the search, and select a search frequency. After the user submits this information they recieve an enrollment email with the most recent results. They then receive an email when new results are found. All emails include a link to unsubscribe from the search.

A JSON file is created for each search. Existing searches are loaded and scheduled when the server starts.

## Setup

#### Configuration
Prior to building, rename `src/main/resources/config.json.example` to `config.json`, and set the following properties:

* `email_username` - Username for the account to send emails to users (tested with Gmail)
* `email_password` - Password for the email account
* `email_name` - Name to use for the sender
* `saved_search_directory` - Absolute path to the folder where searches will be saved (must already exist on the system)
* `max_searches_per_minute` - Searches are rate-limited according to this value to avoid being blacklisted by Craigslist
* `base_url` - The base URL for where the page is hosted, used for adding unsubscribe links to emails

#### Build and Deploy
Build with Maven (`mvn package`). Deploy the resulting WAR file (from `/target` directory) to a servlet container. Tested with Tomcat and Glassfish.

## Issues
* Servlet container will still start even if config fails to load correctly (Runtime exception is logged in the console). This means the servlets still respond to requests, but none of the back end processes will run correctly. 

## Future work
* Better validation of search URLs.
* Logging of searches and errors.
* Add HTML scraping. RSS feeds are delayed by approximately one hour, so the newest postings are currently not found.
* Add proxy support to get around Craigslist's request limit. 
