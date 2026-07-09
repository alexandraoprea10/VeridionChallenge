# **Veridion Challenge - Website Technologies Scraper**

## **Overview**
The goal was to build a program capable of **detecting all technologies used to build a website**.

    Final metrics: 200 domains | 447 unique technologies discovered

## **Architecture**
The project was implemented in Java because it is the language I am most familiar with. This allowed me to **efficiently build
features** and would have been more time-consuming to implement in other languages. The system utilizes Java's predefined class
**HttpClient** to handle network requests and replies.
As an initial step, I **converted the original domain dataset into a standard .txt file** ("domains.txt"). This made it much easier
to process the list of websites.

## **Implementation**
The application first reads the domains.txt file and converts it into a list of strings containing the target websites. For each domain
in this list, the program executes a search function (verifyTechnologies) that **inspects all data received from the HttpClient request**.

## **Class Explanation**

**Main**

This is the entry point of the application. It controls the execution and starts the **scanning process for each domain**.
For counting all Technologies, the system uses a **Set instead of a List**, because it does not allow duplicates.

**Technology Class**

* The class represents the data structure for a technology's profile.
  It contains separate fields for every location where I look for technology indicators

      HTML content, cookies, headers, scripts, meta, text

* It also includes getters (made the fields private) and helper methods to add items for these lists.

**CreateRulesAndDomains Class**

* To build the rule system, I searched on the internet and found a repository containing a **complete list of web technologies**
  and their features in a JSON format


      https://github.com/tylerpuig/wapalyzer-core/blob/main/technologies.json


* I downloaded this JSON database and used it to create the rules into the system. **CreateRules function** reads the **downloaded
  technology signatures JSON file** and prepares all the rules for the matching phase. This is the part where I figured
  what fields I should analyze in the **GET response**. I also created a function named **createDomains**
  that reads the "domains.txt" file and returns the final **list of websites** to be scanned.

**VerifyRules Class**

* I created two **helper functions (createHttpURL and createHttpsURL)** that take a raw domain and format it into a full URL Link where
  the Http Client can connect to. The main part of the project is created in the verifyTechnologies function. This handles the
  entire ecosystem of a website.
* Inside this function the program performs all the scanning. It uses Java's HttpClient to send a
  **GET request** to the target website. To prevent infinite looping(in case of a non-working website) I put a 20 second connection
  timeout.
* Once the server responds, the function takes the response body and the headers. The first 4 rule sets: **html, meta, text,
  script** perform searches inside the exact same source (htmlResponse). **The cookies** are separated and it extracts all incoming Set-Cookie
  fields from the network headers and put them into a **single string** using a delimiter(I used ;). It is easier this way because I
  apply a "contains" rule and I use the memory efficiently. (In this case, complexity **O(1)**).

## **Output**
After the search is complete, the system maps all technologies found into a JSONArray. It creates a directory named /outputs and
individual answer files for each domain.
The output is divided in two parts:
* **The /outputs folder**: All individual results are stored inside the directory. Each file contains a JSON list (the task required to be
  a JSON, CSV or Parquet file) showing all technologies found on that particular website.
* **The numberOfTechnologies File**: The file is located at the project root(not in the /outputs directory). It provides the total count
  of unique technologies found across all 200 domains, with no duplicates.


## **Debate Topics**
* The **main issues with the implementations** are quite different. For example, I did not know where I should search besides html content,
  cookies and headers. I managed to figure it out after reviewing the "technologies.json" file and seeing that there are some more fields
  where I can look for information. The system does not implement error catching for common network failures, especially **DNS errors**. I tried
  adding another field in the Technology class and I tried to process it but the implementation was wrong. To solve this, I need to wrap the
  HttpClient request in a proper **try-catch block that intercepts Java's UnknownHostException**, logs the error, and safely skips to the next domain
  without crashing. Another network failure is the **timeout rule**. The thread waits for 20 seconds if a website is blocked, but for large lists,
  the process is being slowed. To fix this, I want to use **multithreading**(explained below).

* Right now, the program checks websites one by one. If it has to check millions of sites, it would take much more time than it does right
  now. To fix this, I would change the code so the program can check hundreds of websites at the exact same time. For example, I would use
  a **Sliding Window strategy with multithreading**. This means that the program will scan a fixed number of websites at the exact same time. The
  moment one website finishes, the window slides forward. This keeps the system working at maximum speed. I can also distribute the processing across
  **multiple computers** One computer cannot handle many sites alone. This would keep servers working efficiently at the same time.

* To discover new technologies in the future, I would use an **AI to write new rules**. I could send the code of some websites and have an LLM watch
  the code behind the website. It can find a pattern and write JSON rules for me. I could also create a **script** that can automatically search on
  the internet for **new websites or popular tools**, extract their HTML code and send it directly to the LLM to **update the rule list by itself**.

## **How to run the program**
The program uses **Maven** for dependencies, so make sure you have it installed. To compile the program, open your terminal in the root
directory and use the **make** command. You can also compile the Main class **manually** pressing the **run** button.

      make