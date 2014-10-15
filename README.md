## About

 GogoGlasto a utility for bypassing the Glastonbury Festival registration page by brute force.  It should be effective
 on any websites where the probability of success is fn(number_of_page_refreshes_made), particularly seetickets.com events.

 I'm sharing this project as a form of protest - because the current "refresh lottery" system is completely unfair.

 A more fair system, in my mind, would require all pre-registered users to login before the sale begins. A websocket
 would be opened to the dealer who will call back the user if they are selected.  This way allocation can be weighted more
 toward regular attendees and those who missed out the previous year.

 A less fancy alternative would be to just email out invite codes until there are no tickets left.


### Requirements

 - Java 7

 To build the jar from source yourself, you also need jdk7 and maven. Just run the command below to output the jar.

    mvn package

### Usage

    usage: --url URL [options]

    -A,--user-agent <AGENT>
    -e,--expected-text <TEXT>      stop if page contains TEXT
    -h,--holding-text <TEXT>       stop if page does not contain TEXT
    -n,--num-workers <THREADS>     Number of worker threads
    -r,--response-time-out <MILLISECONDS>     server must reply in MILLISECONDS (1000)
    -t,--connection-time-out <MILLISECONDS>   connection must be made in MILLISECONDS (1000)
       --url <URL>                 URL of the page to test

    example usage:

    java -jar gogoglasto-1.0-SNAPSHOT.jar --url http://sign-up-page.com --holding-text "holding" -n 1 --user-agent "Mozilla firefox..."

    Please use the -n, -r and -t options responsibly.

Once the holding page has been cleared, you will see some text like this:

    Paste the javascript line below into your address bar to copy the cookies:

    javascript:(function(){document.cookie="session_id=2fdq47uit0io0l4cconhnh4do1";alert("cookie set!");location.reload();})()

This text should be pasted into the address bar of your browser __while on registration page.__

## Notes

- [Chrome] removes the "javascript:" part of the text when you paste it in the address bar. re-type it manually if needed.
- [Safari] doesn't allow javascript in the address bar at all unless you manually allow it in the developer options.

## Future Ideas

- Replace the threaded http client with an event based (async) one so we can scale better.
- Support proxies
- Make some kind of browser extension.



