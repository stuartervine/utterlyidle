package com.googlecode.utterlyidle.handlers;

import com.googlecode.utterlyidle.Entity;
import com.googlecode.utterlyidle.HeaderParameters;
import com.googlecode.utterlyidle.Response;
import com.googlecode.utterlyidle.Status;
import org.junit.Test;

import static com.googlecode.totallylazy.Option.none;
import static com.googlecode.utterlyidle.Entities.inputStreamOf;
import static com.googlecode.utterlyidle.HttpHeaders.CONTENT_LENGTH;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

public class ContentLengthHandlerTest {
    @Test
    public void doesNotSetContentLengthForNotModifiedResponses() throws Exception {
        Response response = ContentLengthHandler.setContentLength(Response.response(Status.NOT_MODIFIED));
        assertThat(response.headers().valueOption(CONTENT_LENGTH), is(none(String.class)));
    }

    @Test
    public void doesNotSetContentLengthForNoContentResponses() throws Exception {
        Response response = ContentLengthHandler.setContentLength(Response.response(Status.NO_CONTENT));
        assertThat(response.headers().valueOption(CONTENT_LENGTH), is(none(String.class)));
    }

    @Test
    public void doesNotSetContentLengthForInformationalResponses() throws Exception {
        Response response = ContentLengthHandler.setContentLength(Response.response(Status.CONTINUE));
        assertThat(response.headers().valueOption(CONTENT_LENGTH), is(none(String.class)));
    }

    @Test
    public void ifContentLengthIsPresentEnsureEntityIsByteArrayAndContentLengthIsCorrect() throws Exception {
        Entity entity = Entity.entity(inputStreamOf("Hello"));
        HeaderParameters parameters = ContentLengthHandler.setContentLength(entity, HeaderParameters.headerParameters().add(CONTENT_LENGTH, "1"));
        assertThat(parameters.getValue(CONTENT_LENGTH), is("5"));
        assertThat(entity.value(), is(instanceOf(byte[].class)));
    }

    @Test
    public void ifContentLengthIsNotPresentAndEntityLengthIsKnownEnsureContentLengthIsCorrect() throws Exception {
        Entity entity = Entity.entity("Hello");
        HeaderParameters parameters = ContentLengthHandler.setContentLength(entity, HeaderParameters.headerParameters());
        assertThat(parameters.getValue(CONTENT_LENGTH), is("5"));
        assertThat(entity.value(), is(instanceOf(byte[].class)));
    }
}
