import React, { useState } from 'react';
import axios from 'axios';

const FileUpload = () => {
  const [file, setFile] = useState(null);
  const [uploadedUrl, setUploadedUrl] = useState(null);
  const [uploading, setUploading] = useState(false);
  const [error, setError] = useState(null);
  const generatedUrl = 'https://example.com/uploaded-image.jpg';
    setUrl(generatedUrl);
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


      const response = await axios.post('http://localhost:8080/upload/image', formData, {
        headers: {
          'Content-Type': 'multipart/form-data',

        }
      });


      setUploadedUrl(response.data);
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
      {uploadedUrl && (
        <div>
          <p>File uploaded successfully:</p>
          <a href={uploadedUrl} target="_blank" rel="noopener noreferrer">{uploadedUrl}</a>
        </div>
      )}
    </div>
  );
};

export default FileUpload;