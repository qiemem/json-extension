JSON Extension
===

Functions:

- **parse** *json: string* - Creates an object from a string of JSON. JSON lists are converted to NetLogo lists. JSON objects are converted to maps that may be interacted with using other functions in this extension. Values go to the obvious NetLogo values, except for null. I'm still not sure what to do with null.

- **item** *key: string or int* *object: map or list* - Gets the item corresponding to *key* in the given object. For instance `json:item "foo" (json:parse "{\"foo\": 5}")` will be 5.

- **nested-item** *object: map or list* *key...: string or int* - Walks down the JSON structure using each successive key. For instance `(json:nested-item (json:parse "{\"foo\": [\"hi\" {\"bar\": 2}]}") "foo" 1 "bar")` will be 2. Note that **item** and **nested-item** have contradictory parameter orders. I want to fix that, but I'm not sure which is better.

- **keys** *object: map* - Returns the list of keys in the map.

- **values**

- **member?**