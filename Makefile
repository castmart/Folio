.PHONY: test
test:
	./gradlew test

.PHONY: format-code
format-code:
	./gradlew ktlintFormat


.PHONY: unit-tests
unit-tests:
	./gradlew :core:test & ./gradlew :details:test

.PHONY: integration-tests
	./gradlew :integration-tests:test

.PHONY: e2e-tests
e2e-tests:
	./gradlew :configuration:test
