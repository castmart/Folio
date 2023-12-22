.PHONY: test
test:
	./gradlew test

.PHONY: format-code
format-code:
	./gradlew ktlintFormat