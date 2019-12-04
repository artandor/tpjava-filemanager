package com.rizomm.filemanager.business.services;

import com.rizomm.filemanager.business.entities.connectionsimpl.FtpConnection;
import com.rizomm.filemanager.business.repositories.FtpConnectionRepository;
import lombok.Data;
import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Optional;

@Service
@Data
public class FtpService {
    private static FTPClient ftp;
    final FtpConnectionRepository ftpConnectionRepository;
    public FtpConnection ftpConnection;

    public FtpService(FtpConnectionRepository ftpConnectionRepository) {
        this.ftpConnectionRepository = ftpConnectionRepository;
    }

    public FtpConnection save(FtpConnection connection) {
        return ftpConnectionRepository.save(connection);
    }

    public List<FtpConnection> getAll() {
        return ftpConnectionRepository.findAll();
    }

    public Optional<FtpConnection> findFirstById(long id) {
        return ftpConnectionRepository.findFirstById(id);
    }

    public void initialize() throws IOException {
        if (ftpConnection == null) {
            return;
        }
        ftp = new FTPClient();

        ftp.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));

        ftp.connect(this.ftpConnection.getHost(), this.ftpConnection.getPort());
        int reply = ftp.getReplyCode();
        if (!FTPReply.isPositiveCompletion(reply)) {
            ftp.disconnect();
            throw new IOException("Exception in connecting to FTP Server");
        }

        ftp.login(this.ftpConnection.getUsername(), this.ftpConnection.getPassword());
        System.out.println("Connected to FTP Server.");
    }

    public void close() throws IOException {
        ftp.disconnect();
    }

    public String putFileToPath(String fileName, File file) throws IOException {
        ftp.storeFile(this.ftpConnection.getDocumentRoot() + fileName, new FileInputStream(file));
        return fileName;
    }
}
