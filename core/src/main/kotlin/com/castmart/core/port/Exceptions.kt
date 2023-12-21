package com.castmart.core.port

class EntityNotFoundException(override val message: String = "Not Found"): NoSuchElementException(message)