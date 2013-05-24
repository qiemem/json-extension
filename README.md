JSON Extension
===

## Functions

- **parse** *string* - Creates an object from a string of JSON. JSON lists are converted to NetLogo lists. JSON objects are converted to maps that may be interacted with using other functions in this extension. Values go to the obvious NetLogo values, except for null. I'm still not sure what to do with null.

- **item** *(string or int)* *(object or list)* - Gets the item corresponding to *key* in the given object. For instance `json:item "foo" (json:parse "{\"foo\": 5}")` will be 5.

- **nested-item** *(object or list)* *(string or int)...* - Walks down the JSON structure using each successive key. For instance `(json:nested-item (json:parse "{\"foo\": [\"hi\" {\"bar\": 2}]}") "foo" 1 "bar")` will be 2. Note that **item** and **nested-item** have contradictory parameter orders. I want to fix that, but I'm not sure which is better.

- **keys** *object* - Returns the list of keys in the object.

- **values** *object* - Returns the list of values in the object.

- **member?** *string* *object* - Returns true if and only if the given string is a key in the given object.

## Build

1. Set the `SCALA_HOME` environment to the location of your Scala installation. That is, `$SCALA_HOME/bin/scalac` should be your Scala compiler.
    - For compatibility with NetLogo 5.0.x, this extension should be compiled with Scala 2.9.x.
    - For me, using Homebrew installed Scala, this is `/usr/local/Cellar/scala29/2.9.2`
2. Set `NETLOGO` to the location of your NetLogo installation. `netlogo.jar` should be contained in this folder.
    - Mine is `/Applications/NetLogo 5.0.4/`
3. Run `make` from inside your local clone of this repository.
4. Move the resulting `json.jar` to the `$NETLOGO/extensions/json/` directory (you will have to create the `json` directory).
