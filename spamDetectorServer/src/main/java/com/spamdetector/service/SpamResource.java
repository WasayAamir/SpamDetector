package com.spamdetector.service;

import com.spamdetector.domain.TestFile;
import com.spamdetector.util.SpamDetector;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import java.io.File;

import java.util.List;

import jakarta.ws.rs.core.Response;

@Path("/spam")
public class SpamResource {

    //    your SpamDetector Class responsible for all the SpamDetecting logic
    SpamDetector detector;

    public SpamResource()
    {
//      TODO: load resources, train and test to improve performance on the endpoint calls
        this.detector = new SpamDetector();
        System.out.print("Training and testing the model, please wait");
//      TODO: call  this.trainAndTest();
        this.trainAndTest();
    }

    @GET
    @Produces("application/json")
    public Response getSpamResults()
    {
        List<TestFile> testResults = trainAndTest();
        Response response = Response.status(200)
                .header("Access-Control-Allow-Origin", "http://localhost:63342")
                .header("Content-Type", "application/json")
                .entity(testResults.toArray(new TestFile[0]))
                .build();
        return response;
    }

    @GET
    @Path("/accuracy")
    @Produces("application/json")
    public Response getAccuracy() {
//      TODO: return the accuracy of the detector, return in a Response object
        double accuracy = detector.calcAccuracy();
        /* Convert testResults to JSON */;
        Response response = Response.status(200)
                .header("Access-Control-Allow-Origin", "http://localhost:63342")
                .header("Content-Type", "application/json")
                .entity(accuracy)
                .build();
        return response;
    }

    @GET
    @Path("/precision")
    @Produces("application/json")
    public Response getPrecision() {
        //TODO: return the precision of the detector, return in a Response object
        double precision = detector.calcPrecision();
        Response response = Response.status(200)
                .header("Access-Control-Allow-Origin", "http://localhost:63342")
                .header("Content-Type", "application/json")
                .entity(precision)
                .build();
        return response;
    }

    private List<TestFile> trainAndTest()
    {
//        TODO: load the main directory "data" here from the Resources folder
        File mainDirectory = new File(getClass().getClassLoader().getResource("data").getFile());
        return detector.trainAndTest(mainDirectory);
    }
}
