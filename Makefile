all: clean compile run

compile:
	mvn compile

run:
	mvn exec:java -Dexec.mainClass="org.example.Main"

clean:
	mvn clean
	rm -rf outputs
	rm -f numberOfTechnologies