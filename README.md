# PDF Merger

[![status-badge](https://ci.kisoft.me/api/badges/1/status.svg)](https://ci.kisoft.me/repos/1)

An HTTP API to merge multiple PDFs in one. Supports Multipart Requests and Base64 JSON Requests


## Installation

run the command ``docker run -p 7000:7000 kisoft/pdf-merger`` to spin up 
a new instance. 

## Usage

### Multipart

Make a ``POST`` request to ``/merge-multipart`` with the PDF files
you want to merge together.

### Base64

Make a ``POST`` request to ``/merge-base64`` with the JSON structure

```json
{
 "files": string[]
}

```
where each file is a base64 encoded PDF file in the ``files`` array. 

## Test Coverage
TODO
## Notes
TODO

