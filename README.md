[![](https://jitpack.io/v/Mobile-Innowise-Group/DateTime-Reel-Picker.svg)](https://jitpack.io/#Mobile-Innowise-Group/DateTime-Reel-Picker)
# DateTime-Reel-Picker

A library that provides a reel picker for date and time.
Can be used in 3 ways: 
- date picker only,
- time picker, 
- date and time picker.
Contains properties for setting date and time min and max ranges.
Also supports input from keyboard, date and time checks and validations


![collage](./collage.jpg)


<br/>


![](./sample.gif)


<br/>
<br/>

## Download


Add below lines to root's build.gradle
```gradle
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
```    

Grab via Gradle


```gradle
implementation 'com.github.Mobile-Innowise-Group:DateTime-Reel-Picker:{latest version}'
```

<br/>

## Description

| Method | What it does |
|----------------|---------|
| createTimeDialog | create a dialog where you can select only time |
| createDateDialog | create a dialog where you can select only date |
| createDateTimeDialog | create a dialog where you can select both date and time |
| setOkClickCallback | ok button click callback |
| setCancelClickCallback | cancel button click callback |
| showDialog | show picker dialog with a chosen type |

<br/>

## Time and Date internal entities

<br/>
LocalTime methods:


| Method | What it does |
|-------------|-------------|
| now | set the current time |
| of | set a specific time |
| plusMinutes | add minutes to the selected time |
| minusMinutes | subtract minutes from the selected time |
| plusHours | add hours to the selected time |
| minusHours | subtract hours from the selected time |
| hour | get selected hour |
| minute | get selected minute |

<br/>
LocalDate methods:


| Method | What it does |
|------------|--------------|
| now | set the current date |
| of | set a specific date |
| plusDays | add days to the selected date |
| minusDays | subtract days from the selected date |
| plusMonths | add months to the selected date |
| minusMonths | subtract months from the selected date |
| plusYears | add years to the selected date |
| minusYears | subtract years from the selected date |
| day | get selected day |
| month | get selected month |
| year | get selected year |

<br/>
LocalDateTime methods:


| Method | What it does |
|-------------|-----------------------|
| now | set the current date and time |
| of | set a specific date and time |
| toLocalDate | get only the date from LocalDateTime value |
| toLocalTime | get only the time from LocalDateTime value |


<br/>


## Usage


### __DateDialog Example:__


#### Default implementation

#### **MainActivity.kt**
```kotlin
ReelPicker
    .createDateDialog()
    .setOkClickCallback(object : ReelPicker.OkClickCallback<LocalDate> {
        override fun onOkClick(value: LocalDate) {
            //do whatever you want with callback value
        }
    })
    .setCancelClickCallback(object : ReelPicker.CancelClickCallback{
        override fun onCancelClick() {
            //do whatever you want
        }
    })
    .showDialog(supportFragmentManager)
```

#### **MainActivity.java**
```java
ReelPicker
        .createDateDialog()
        .setOkClickCallback(value -> {
            //do whatever you want with callback value
        }).setCancelClickCallback(() -> {
            //do whatever you want
        }).showDialog(fragmentManager)
```
<br/>

#### Custom implementation

```kotlin
ReelPicker
    .createDateDialog(
        initialLocalDate = LocalDate.now().plusMonths(11).minusYears(1),
        minLocalDate = LocalDate.of(13,1,2055).minusDays(5).plusMonths(3),
        maxLocalDate = LocalDate.now().minusMonths(19)
    )
    .setOkClickCallback(object : ReelPicker.OkClickCallback<LocalDate> {
        override fun onOkClick(value: LocalDate) {
            //do whatever you want with callback value
        }
    })
    .setCancelClickCallback(object : ReelPicker.CancelClickCallback{
        override fun onCancelClick() {
            //do whatever you want
        }
    })
    .showDialog(supportFragmentManager)
```

#### **MainActivity.java**

```java
ReelPicker
        .createDateDialog(
            LocalDate.now().plusMonths(11).minusYears(1),
            LocalDate.of(13,1,2055).minusDays(5).plusMonths(3),
            LocalDate.now().minusMonths(19)
        )
        .setOkClickCallback(value -> {
            //do whatever you want with callback value
        })
        .setCancelClickCallback(() -> {
            //do whatever you want
        })
        .showDialog(fragmentManager)
```

<br/>


### __TimeDialog Example:__


#### Default implementation

#### **MainActivity.kt**
```kotlin
ReelPicker
    .createTimeDialog()
    .setOkClickCallback(object : ReelPicker.OkClickCallback<LocalTime> {
        override fun onOkClick(value: LocalTime) {
            //do whatever you want with callback value
        }
    })
    .setCancelClickCallback(object : ReelPicker.CancelClickCallback{
        override fun onCancelClick() {
            //do whatever you want
        }
    })
    .showDialog(supportFragmentManager)
```

<br/>

#### **MainActivity.java**
``` java
ReelPicker
        .createTimeDialog()
        .setOkClickCallback(value -> {
            //do whatever you want with callback value
        }).setCancelClickCallback(() -> {
            //do whatever you want
        }).showDialog(fragmentManager)
```


<br/>

#### Custom implementation

``` kotlin
ReelPicker
    .createTimeDialog(
        initialLocalTime = LocalTime.of(15, 20).minusMinutes(62),
        minLocalTime = LocalTime.of(3, 18).plusHours(2),
        maxLocalTime = LocalTime.of(22, 52).plusMinutes(30)
    )
    .setOkClickCallback(object : ReelPicker.OkClickCallback<LocalDate> {
        override fun onOkClick(value: LocalDate) {
            //do whatever you want with callback value
        }
    })
    .setCancelClickCallback(object : ReelPicker.CancelClickCallback{
        override fun onCancelClick() {
            //do whatever you want
        }
    })
    .showDialog(supportFragmentManager)
```

#### **MainActivity.java**

``` java
ReelPicker
        .createTimeDialog(
            LocalTime.of(15, 20).minusMinutes(62),
            LocalTime.of(3, 18).plusHours(2),
            LocalTime.of(22, 52).plusMinutes(30)
        )
        .setOkClickCallback(value -> {
            //do whatever you want with callback value
        })
        .setCancelClickCallback(() -> {
            //do whatever you want
        })
        .showDialog(fragmentManager)
```

<br/>

### __DateTimeDialog Example:__


#### Default implementation

#### **MainActivity.kt**
``` kotlin
ReelPicker
    .createDateTimeDialog()
    .setOkClickCallback(object : ReelPicker.OkClickCallback<LocalDateTime> {
        override fun onOkClick(value: LocalDateTime) {
            //do whatever you want with callback value
        }
    })
    .setCancelClickCallback(object : ReelPicker.CancelClickCallback{
        override fun onCancelClick() {
            //do whatever you want
        }
    })
    .showDialog(supportFragmentManager)
```

<br/>

#### **MainActivity.java**
``` java
    ReelPicker
        .createDateTimeDialog()
        .setOkClickCallback(value -> {
            //do whatever you want with callback value
        })
        .setCancelClickCallback(() -> {
            //do whatever you want
        })
        .showDialog(fragmentManager);
```

<br/>


#### Custom implementation

``` kotlin
    ReelPicker
        .createDateTimeDialog(
            initialLocalDateTime = LocalDateTime.now(),
            minLocalDateTime = LocalDateTime.of(
                LocalDate.of(1, Month.MAY, 1950),
                LocalTime.now().minusHours(40)
            ),
            maxLocalDateTime = LocalDateTime.of(
                LocalDate.now().plusYears(15),
                LocalTime.of(23, 33)
            )
        )
        .setOkClickCallback(object : ReelPicker.OkClickCallback<LocalDateTime> {
            override fun onOkClick(value: LocalDateTime) {
                //do whatever you want with callback value
            }
        })
        .setCancelClickCallback(object : ReelPicker.CancelClickCallback{
            override fun onCancelClick() {
                //do whatever you want
            }
        })
        .showDialog(supportFragmentManager)
```

#### **MainActivity.java**

``` java
    ReelPicker
        .createDateTimeDialog(
            LocalDateTime.now(),
            LocalDateTime.of(
                LocalDate.of(1, Month.MAY, 1950),
                LocalTime.now().minusHours(40)
            ),
            LocalDateTime.of(
                LocalDate.now().plusYears(15),
                LocalTime.of(23, 33)
            )
        )
        .setOkClickCallback(value -> {
            //do whatever you want with callback value
         })
        .setCancelClickCallback(() -> {
            //do whatever you want
         })
        .showDialog(fragmentManager);
```


<br/>
<br/>

# Sample

[Sample (MainActivity.kt)](https://github.com/Mobile-Innowise-Group/DateTime-Reel-Picker/blob/main/app/src/main/java/com/innowisegroup/datetimepicker/MainActivity.kt)

<br/>

# License

MIT License


Copyright (c) 2019 Innowise Group


Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:


The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.


THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
