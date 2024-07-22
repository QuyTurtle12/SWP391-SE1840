import React, { useState, useEffect } from 'react';
import axios from 'axios';

const FileUpload = ({ setImg }) => {
  const [file, setFile] = useState(null);
  const [uploadedUrl, setUploadedUrl] = useState(null);
  const [uploading, setUploading] = useState(false);
  const [error, setError] = useState(null);

  const handleFileChange = (e) => {
    setFile(e.target.files[0]);
  };

  const handleUpload = async () => {
    setUploading(true);
    setError(null);

    try {
      const token = localStorage.getItem('token'); // Retrieve token from localStorage
      const formData = new FormData();
      formData.append('file', file);

      const response = await axios.post('https://jewelrysalesystem-backend.onrender.com/upload/image', formData, {
        headers: {
          'Content-Type': 'multipart/form-data',
          'Authorization': `Bearer ${token}`, // Add Authorization header with token
        }
      });

      setUploadedUrl(response.data);
      setImg(response.data); // Call the setImg callback with the uploaded URL
    } catch (error) {
      console.error('Error uploading file: ', error);
      setError('Failed to upload file.');
    } finally {
      setUploading(false);
    }
  };

  return (
    <div>
      <input type="file" onChange={handleFileChange} />
      <button onClick={handleUpload} disabled={!file || uploading}>
        {uploading ? 'Uploading...' : 'Upload'}
      </button>

      {error && <p style={{ color: 'red' }}>{error}</p>}
      {uploadedUrl && !uploading && (
        <div>
          <p>File uploaded successfully</p>
        </div>
      )}
    </div>
  );
};

export default FileUpload;
