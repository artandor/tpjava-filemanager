package com.rizomm.filemanager.business.services;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.rizomm.filemanager.business.entities.connectionsimpl.AmazonS3Connection;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

import static com.rizomm.filemanager.business.utils.Utils.convertMultiPartToFile;
import static com.rizomm.filemanager.business.utils.Utils.generateFileName;

@Service
public class AmazonS3Service {
    public AmazonS3Connection connection;

    private AmazonS3 s3Client;

    public void initialize() {
        BasicAWSCredentials credentials = new BasicAWSCredentials(this.connection.getAccessKey(), connection.getSecretKey());
        AwsClientBuilder.EndpointConfiguration endpointConfiguration = new AwsClientBuilder.EndpointConfiguration(connection.getHost(), "");
        this.s3Client = AmazonS3ClientBuilder.standard()
                .withEndpointConfiguration(endpointConfiguration)
                .withPathStyleAccessEnabled(true)
                .withCredentials(new AWSStaticCredentialsProvider(credentials)).build();
    }

    private void uploadFileTos3bucket(String fileName, File file) {
        try {
            System.out.println("Uploading a new object to S3 from a file\n");
            this.s3Client.putObject(new PutObjectRequest(this.connection.getBucket(), fileName, file)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
        } catch (AmazonServiceException ase) {
            System.err.println("Caught an AmazonServiceException, which means your request made it to Amazon S3, but" +
                    " was rejected with an error response for some reason.");
            System.err.println("Error Message:    " + ase.getMessage());
            System.err.println("HTTP Status Code: " + ase.getStatusCode());
            System.err.println("AWS Error Code:   " + ase.getErrorCode());
            System.err.println("Error Type:       " + ase.getErrorType());
            System.err.println("Request ID:       " + ase.getRequestId());

        } catch (AmazonClientException ace) {
            System.err.println("Caught an AmazonClientException, which " + "means the client encountered " + "an internal error while trying to "
                    + "communicate with S3, " + "such as not being able to access the network.");
            System.err.println("Error Message: " + ace.getMessage());

        }
    }

    public String uploadFile(MultipartFile multipartFile) {

        String fileUrl = "";
        try {
            File file = convertMultiPartToFile(multipartFile);
            String fileName = generateFileName(multipartFile);
            fileUrl = this.connection.getHost() + "/" + this.connection.getBucket() + "/" + fileName;
            uploadFileTos3bucket(fileName, file);
            file.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileUrl;
    }

    public String deleteFile(String fileName) {
        this.s3Client.deleteObject(new DeleteObjectRequest(this.connection.getBucket() + "/", fileName));
        return "Successfully deleted";
    }
}
