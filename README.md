# Shift
Shift is a development tool that allows you to modify variables and run blocks of code while your Android application is running.

![](https://github.com/coursera/shift/blob/master/images/shiftswitch.gif)
## Why
Have you ever needed to temporarily disable a feature during development or hide UI elements? How about trying out different values for animations, switching between API endpoints, or changing the color or margins of UI components? Wouldn't it be nice to be able to repeatedly call certain API's, clear your apps persistent storage, or reset user preferences at anytime in your app? What about sending a screenshot of a bug in your app through email?

Shift helps us with all of these things through the use of ShiftValues and ShiftActions and provides an easy to implement UI to modify values and run actions.

**`ShiftValue`**

 - A value of type String (**ShiftString**), boolean(**ShiftBoolean**), int(**ShiftInt**), or float(**ShiftFloat**) that can be modified while the app is running. 

**`ShiftAction`**

 - A wrapper class for a Java Runnable and a String describing what the action does. 
 
All features at Coursera are wrapped around a ShiftBoolean. This allows developers to disable or enable a feature if it is not meant to be released yet and continue working without their code disrupting others. 

Shift continues to be immensely helpful for building the Coursera Android application and we hope it will benefit others as well.

#Usage
Shift has 3 main components:
   
**`ShiftLauncherView`**

 - Creates the User Interface for Shift in your Android app that the user will interact with. Provides a floating icon and a menu for configuring your ShiftValues and ShiftActions
   
**`ShiftManager`**

 - Used to initialize Shift, create a ShiftLauncherView, subscribe to changes in ShiftValues, and register ShiftActions

**`ShiftVisibilityClient`**

 - Decides whether or not the ShiftLauncherView should be visible to the user
 - You must implement this interface and provide it to ShiftManager on initialization
 
##Quick Start Guide
**Your application must use an AppCompat theme. https://developer.android.com/tools/support-library/features.html** 

``` xml
Eg. Theme.AppCompat.Light.DarkActionBar
```

Initialize Shift in your Application with a Visibility Client and your apps LAUNCHER class
``` java

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ShiftManager.initialize(this, 
	        new SimpleVisibilityClient(this, BuildConfig.DEBUG),
                MainActivity.class);
	}
}
```

Create ShiftValues with a Category (Eg. New Features), a description, the author, whether or not your app should restart when this value is changed, and the default value.
```java
public class ShiftValues {
		public static final ShiftBoolean IS_NEW_DESIGN_ENABLED = new 
		ShiftBoolean("Features","Enable New Design","Stanley Fung",false, false);
}
```
Retrieve the value at anytime in your Fragments, Activity's, or Views
```java
if (ShiftValues.IS_NEW_DESIGN_ENABLED.getBooleanValue()) {
	//Launch page with new design
} else {
	//Launch old page
}
```
To make a ShiftAction that you can run while in your app, create a new ShiftAction and use ShiftManager to register it:

```java
 ShiftManager.getInstance().registerAction(new ShiftAction("Clear Persistent Storage", new Runnable() {
            @Override
            public void run() {
                // Clear Database
            }
        }));

```
Add a line to the Activity you want to show Shift in that creates a ShiftLauncherView. Tapping on the floating icon will bring up the ShiftMenu.
```java
public class MyActivity extends FragmentActivity {
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		      ShiftManager.getInstance()
			      .createShiftViewAndShowFloatingIcon(this);
	}
}

```
**Take a look at the DemoApp for more examples!**

##Detailed Guide

**Your application must use an AppCompat theme.**
Since Shift uses RecyclerView, CardView, and SwitchCompat, your Application or Activity you are using Shift in must use an AppCompat theme or you will get an error like this:

```java
java.lang.IllegalStateException: You need to use a Theme.AppCompat theme (or descendant) with this activity
```

**Initialize Shift in your Application class:**

Shift needs a **ShiftVisibilityClient** to decide whether or not to show itself. 

We have provided a **SimpleVisibilityClient** as a simple implementation using SharedPreferences. You can implement the interface and make your own VisibilityClient if you want something more complicated.

Eg. You need users to login to your app and have your own logic for determining if an account belongs to a developer or employee. Only developer/employee accounts can access Shift.

We'll allow users to see Shift if we are running in DEBUG mode.
```java
SimpleVisibilityClient visibilityClient = new SimpleVisibilityClient(context,  BuildConfig.DEBUG);
```


Now initialize ShiftManager

``` java 
ShiftManager.initialize(this, visibilityClient);
```
If you want your app to restart when certain ShiftValues are changed (we commonly do this for big features under a ShiftBoolean), make an additional call to set your apps LAUNCHER class:

``` java
ShiftManager.getInstance().setLauncherClassForRestart(MainActivity.class);
```

Alternatively you can use the second initialize call:

``` java 
ShiftManager.initialize(this, visibilityClient, MainActivity.class);
```
***Calling ShiftManager.getInstance() before calling ShiftManager.initialize() will throw an exception!***

### Shift Value
There are 4 types of ShiftValues: 

 - ShiftString
 - ShiftBoolean
 - ShiftInteger
 - ShiftFloat


Each ShiftValue takes a Category, Description and Author as Strings, 
a boolean that determines if the application will restart when this ShiftValue is changed, and the default value for this specific ShiftValue.


```java
public class ShiftValues {
		// Category Description Author RestartOnChange DefaultValue
		public static final ShiftString MESSAGE = new 
	ShiftString("Strings","First Words","Stanley Fung",false,"HELLO WORLD");
}
```

To use this value call getStringValue();
```java
textView.text = ShiftValueEntrys.MESSAGE.getStringValue();
```

ShiftValues that have the same Category will be grouped together in the ShiftMenu.

### Subscription

Shift provides 2 ways to subscribe to changes from ShiftValues:

If you want your application to restart when a ShiftValue is modified, set the **4th boolean parameter in the constructor** to true and make sure to provide your apps LAUNCHER class on setup. 
 
```java
ShiftBoolean("Features","Enable New Design","Stanley Fung", true, ...);
```
 
 If you want a specific Activity or  Fragment to refresh itself when certain ShiftValues are changed, extend **ShiftValueListener** and put your refresh logic into **onShiftValuesUpdated(ShiftValue)**. 

**onShiftValuesUpdated(ShiftValue)** is called when the ShiftValues you are subscribed to are changed. You should subscribe to ShiftValues in **onResume()** and unsubscribe in **onPause()**.

The ShiftValue method parameter in onShiftValuesUpdated(ShiftValue) is the specific ShiftValue that was changed. 

If you do not care which value was changed, you can **subscribeToUpdatesForAllShiftValues()** and **unsubscribeToUpdatesForAllShiftValues()** and ignore whichever ShiftValue is passed back.

```java
public class MyActivity extends FragmentActivity implements ShiftValueListener {
	private ShiftValue[] shiftValues = 
		{ShiftValues.END_POINT, ShiftValues.WELCOME_MESSAGE};

		private TextView mWelcomeMessage;
		
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
		    mWelcomeMessage = (TextView) findViewById(R.id.welcome);
		    mWelcomeMessage
			    .setText(ShiftValues.WELCOME_MESSAGE.getStringValue());
	    }

	    @Override
	    public void onResume() {
	        super.onResume();
	        ShiftManager.getInstance()
		        .subscribeToUpdatesForShiftValues(this, shiftValues);
	    }
	
		@Override
	    public void onPause() {
	        super.onPause();
	        ShiftManager.getInstance()
		        .unsubscribeToUpdatesForShiftValues(this, shiftValues);
	    }
		 
		@Override
	    public void onShiftValuesUpdated(ShiftValue shiftValue) {
		    if (shiftValue == ShiftValues.WELCOME_MESSAGE) {
				// Update welcome message text
				String newText = ShiftValues.WELCOME_MESSAGE.getStringValue();
				  mWelcomeMessage.setText(newText);
			}
	    }
}


```
### ShiftAction
Shift Actions allow you to assign a block of code to be run when you click on a button in Shift. Create a new ShiftAction and then call registerAction() from ShiftManager:

```java
 ShiftManager.getInstance().registerAction(new ShiftAction("Performs an Action", new Runnable() {
            @Override
            public void run() {
                // Place your code here
            }
        }));

```

### ShiftLauncherView

In order to actually launch the ShiftMenu that will allow you to modify ShiftValues and run ShiftActions, you must add a line into the Activity you want to show it on.

```java
public class MyActivity extends FragmentActivity {
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		      ShiftManager.getInstance()
			      .createShiftViewAndShowFloatingIcon(this);
	}
}
```
That’s it! For convenience we have provided **ShiftFragmentActivity**, **ShiftActionBarActivity**, and **ShiftAppCompatActivity** for you to inherit. This could be more convenient if you want to launch Shift in every activity in your app.

```java
public class MyActivity extends ShiftFragmentActivity {
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //As long as you call super.onCreate(), the rest will be handled for you 
	}
}
```

If you want to hook up your own button to launch ShiftMenu, create a ShiftView using and call **showShiftMenu()** on click:

```java
  Button shiftMenuButton = new Button(this);
  shiftMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
	              mShiftLauncherView
		              .showShiftMenu(ShiftExampleActivity.this);
            }
        });
```

## Report A Bug Tool

This is an extra feature we included that we use during testing.
Every time you launch the ShiftMenu, Shift will take a screenshot of the current screen and you have the option to send this screenshot through your choice of email client to someone by navigating to the **REPORT** tab. It will include the device name/type and what SDK it is on.

![](https://github.com/coursera/shift/blob/master/images/shiftreport.gif)

##Styling

Shift will color itself based on your Theme.AppCompat's accent color. 

```xml
<style name="CustomAppTheme" parent="Theme.AppCompat.Light.DarkActionBar">
        <item name="colorAccent">#FB8C00</item>
</style>
```
##Installation

####Manual

Clone the repo and then open your Android Studio project. Go to : **File -> Project Structure -> Add a New Module (+) -> Phone And Tablet Application -> Import Existing Project** and select the shift folder.

####Gradle
Coming soon
 

## Contributing
For bugs, questions and discussions please use the Github Issues.

## License

Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
