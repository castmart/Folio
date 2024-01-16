.PHONY: test
test:
	./gradlew test

.PHONY: format-code
format-code:
	./gradlew ktlintFormat

.PHONY: lint-code
lint-code:
	./gradlew ktlintCheck

.PHONY: unit-tests
unit-tests:
	./gradlew :core:test :details:test

.PHONY: integration-tests
integration-tests:
	./gradlew :integration-tests:test

.PHONY: e2e-tests
e2e-tests:
	./gradlew :configuration:test
