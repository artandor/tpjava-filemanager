package com.rizomm.filemanager.business.repositories;

import com.rizomm.filemanager.business.entities.FileUpload;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileUploadRepository extends JpaRepository<FileUpload, Long> {
}
