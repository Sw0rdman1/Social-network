package com.levi9.socialnetwork.exception;

import static org.springframework.http.HttpStatus.*;
import com.levi9.socialnetwork.exception.customexception.*;
import jakarta.mail.MessagingException;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import com.levi9.socialnetwork.exception.customexception.EntityAlreadyExistsException;
import com.levi9.socialnetwork.exception.customexception.EntityNotFoundException;
import com.levi9.socialnetwork.exception.customexception.FriendRequestException;
import com.levi9.socialnetwork.exception.customexception.PostException;
import com.levi9.socialnetwork.exception.customexception.UserRegistrationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import software.amazon.awssdk.services.cognitoidentityprovider.model.*;


@RestControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiError> handleUserNotFoundException(EntityNotFoundException ex) {
        ApiError apiError = new ApiError(NOT_FOUND, ex.getMessage());
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

    @ExceptionHandler(UsernameExistsException.class)
    public ResponseEntity<ApiError> handleUsernameExistsException(UsernameExistsException ex) {
        ApiError apiError = new ApiError(BAD_REQUEST, ex.awsErrorDetails().errorMessage());
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

    @ExceptionHandler(InvalidParameterException.class)
    public ResponseEntity<ApiError> handleInvalidParameterException(InvalidParameterException ex) {
        ApiError apiError = new ApiError(BAD_REQUEST, ex.awsErrorDetails().errorMessage());
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

    @ExceptionHandler(InvalidPasswordException.class)
    public ResponseEntity<ApiError> handleInvalidPasswordException(InvalidPasswordException ex) {
        ApiError apiError = new ApiError(BAD_REQUEST, ex.awsErrorDetails().errorMessage());
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

    @ExceptionHandler(UserRegistrationException.class)
    public ResponseEntity<ApiError> handleUserRegistrationException(UserRegistrationException ex) {
        ApiError apiError = new ApiError(BAD_REQUEST, ex.getMessage());
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

    @ExceptionHandler(CodeMismatchException.class)
    public ResponseEntity<ApiError> handleCodeMismatchException(CodeMismatchException ex) {
        ApiError apiError = new ApiError(BAD_REQUEST, ex.awsErrorDetails().errorMessage());
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

    @ExceptionHandler(ExpiredCodeException.class)
    public ResponseEntity<ApiError> handleExpiredCodeException(ExpiredCodeException ex) {
        ApiError apiError = new ApiError(BAD_REQUEST, ex.awsErrorDetails().errorMessage());
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiError> handleUserNotFoundException(UserNotFoundException ex) {
        ApiError apiError = new ApiError(BAD_REQUEST, ex.awsErrorDetails().errorMessage());
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

    @ExceptionHandler(NotAuthorizedException.class)
    public ResponseEntity<ApiError> handleNotAuthorizedException(NotAuthorizedException ex) {
        ApiError apiError = new ApiError(BAD_REQUEST, ex.awsErrorDetails().errorMessage());
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

    @ExceptionHandler(CognitoIdentityProviderException.class)
    public ResponseEntity<ApiError> handleCognitoIdentityProviderException(CognitoIdentityProviderException ex) {
        ApiError apiError = new ApiError(BAD_REQUEST, ex.awsErrorDetails().errorMessage());
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

    @ExceptionHandler(EntityAlreadyExistsException.class)
    public ResponseEntity<ApiError> handleEntityAlreadyExistsException(EntityAlreadyExistsException ex) {
        ApiError apiError = new ApiError(BAD_REQUEST, ex.getMessage());
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

    @ExceptionHandler(FriendRequestException.class)
    public ResponseEntity<ApiError> handleFriendRequestException(FriendRequestException ex) {
        ApiError apiError = new ApiError(BAD_REQUEST, ex.getMessage());
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

    @ExceptionHandler(EmptyPostException.class)
    public ResponseEntity<ApiError> handleEmptyPostException(EmptyPostException ex) {
        ApiError apiError = new ApiError(BAD_REQUEST, ex.getMessage());
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

    @ExceptionHandler(GroupRequestException.class)
    public ResponseEntity<ApiError> handleGroupRequestException(GroupRequestException ex) {
        ApiError apiError = new ApiError(BAD_REQUEST, ex.getMessage());
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

    @ExceptionHandler(PostUpdateIllegalPermissionException.class)
    public ResponseEntity<ApiError> handlePostUpdateIllegalPermissionException(PostUpdateIllegalPermissionException ex) {
        ApiError apiError = new ApiError(BAD_REQUEST, ex.getMessage());
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }
    @ExceptionHandler(IllegalDateTimeException.class)
    public ResponseEntity<ApiError> handleIllegalDateTimeFormatException(IllegalDateTimeException ex) {
        ApiError apiError = new ApiError(BAD_REQUEST, ex.getMessage());
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatusCode status,
                                                                  WebRequest request) {
        ApiError apiError = new ApiError(BAD_REQUEST, "Validation error");
        apiError.addValidationError(ex.getBindingResult().getGlobalErrors());
        apiError.addValidationErrors(ex.getBindingResult().getFieldErrors());
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

    @ExceptionHandler(PostException.class)
    public ResponseEntity<ApiError> handlePostException(PostException ex) {
        ApiError apiError = new ApiError(BAD_REQUEST, ex.getMessage());
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

    @ExceptionHandler(PostNoPermissionException.class)
    public ResponseEntity<ApiError> handlePostPermissionException(PostNoPermissionException ex) {
        ApiError apiError = new ApiError(FORBIDDEN, ex.getMessage());
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

    @ExceptionHandler(EmptyCommentException.class)
    public ResponseEntity<ApiError> handleEmptyCommentException(EmptyCommentException ex) {
        ApiError apiError = new ApiError(BAD_REQUEST, ex.getMessage());
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

    @ExceptionHandler(CommentException.class)
    public ResponseEntity<ApiError> handleCommentException(CommentException ex) {
        ApiError apiError = new ApiError(BAD_REQUEST, ex.getMessage());
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

    @ExceptionHandler(CommentNoPermissionException.class)
    public ResponseEntity<ApiError> handleCommentPermissionException(CommentNoPermissionException ex) {
        ApiError apiError = new ApiError(FORBIDDEN, ex.getMessage());
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

    @ExceptionHandler(GroupException.class)
    public ResponseEntity<ApiError> handleGroupException(GroupException ex) {
        ApiError apiError = new ApiError(FORBIDDEN, ex.getMessage());
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

    @ExceptionHandler(MessagingException.class)
    public ResponseEntity<ApiError> handleMessagingException(MessagingException ex) {
        ApiError apiError = new ApiError(INTERNAL_SERVER_ERROR, ex.getMessage());
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

    @ExceptionHandler(EventCreationUnauthorizedException.class)
    public ResponseEntity<ApiError> handleEventException(EventCreationUnauthorizedException ex) {
        ApiError apiError = new ApiError(FORBIDDEN, ex.getMessage());
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

    @ExceptionHandler(EventInvitationException.class)
    public ResponseEntity<ApiError> handleEventInvitationException(EventInvitationException ex) {
        ApiError apiError = new ApiError(BAD_REQUEST, ex.getMessage());
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }
}
