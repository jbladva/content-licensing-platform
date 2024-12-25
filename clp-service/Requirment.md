# Licensing platform

### User Registration

```
public/api/user/v1/signup
public/api/user/v1/login
public/api/user/v1/logout
private/api/user/v1/{userId}
private/api/user/v1/{userType}
private/api/user/v1/update
private/api/user/v1/delete
```

## Writer

### Content Upload

- Authenticated writer can upload different type of content.
- Support multiple formats for content submission, including plain text, proprietary **XML, BITS/JATS, EPUB, Word**,
  etc.
- The content we will gonna store in **S3** bucket.
- send notification to publisher for review the content by **kafka**.

```
Post : private/api/writer/content/upload
Get : private/api/writer/content/status
Get : private/api/writer/content/show
Put : private/api/writer/content/update
Post : private/api/writer/content/publish/request?contentId=id&pubId=id
```

## Publisher

```
Post : private/api/publisher/content/review
Post: private/api/publisher/content?accept=true&reject=true
Get : private/api/publisher/content/license-key
Put : private/api/publisher/content?publish=true & unbpublish=true
```

- publisher can review the content which we will share in presigned url.
- publisher can accept or reject the content and based on the action will send the notification to publisher or writer.
- publisher can request for license key which will generate by system.
- publisher can publish the approved content with the appropriate license-key.
- System will map the relation between publisher and writer ad a licensor and license.
- In publish api we will stream data to different platform.


# Database Schema for Licensing Platform
 As we have to do the relations between entity we should follow the sql Database

## Users Table

| Column Name | Data Type                   | Description                 |
|-------------|-----------------------------|-----------------------------|
| UserID      | INT                         | Primary key, unique user ID |
| Username    | VARCHAR(50)                 | Username                    |
| Password    | VARCHAR(100)                | Hashed password             |
| Email       | VARCHAR(100)                | Email address               |
| Role        | ENUM('writer', 'publisher') | Role of the user            |

## Writer Table

| Column Name | Data Type    | Description                 |
|-------------|--------------|-----------------------------|
| Id          | INT          | Primary key, unique user ID |
| Name        | VARCHAR(50)  | Name                        |
| Address     | VARCHAR(100) | Address                     |
| BioDate     | VARCHAR(100) | BioData                     |
| userId      | INT          | User Table Id(foreign key)  |

## Publisher Table

| Column Name      | Data Type    | Description                 |
|------------------|--------------|-----------------------------|
| Id               | INT          | Primary key, unique user ID |
| Name             | VARCHAR(50)  | Name                        |
| Address          | VARCHAR(100) | Address                     |
| BioDate          | VARCHAR(100) | BioData                     |
| OrganizationName | VARCHAR(100) | Name of Organization        |
| OrganizationSite | VARCHAR(100) | Name of Organization        |
| UserId           | INT          | User Table Id(foreign key)  |

## Content

| Column Name     | Data Type    | Description                                                    |
|-----------------|--------------|----------------------------------------------------------------|
| ContentID       | INT          | Primary key, unique content ID                                 |
| WriterId        | INT          | Foreign key referencing Writer.WriterId, identifies the writer |
| Title           | VARCHAR(100) | Title of the content                                           |
| Description     | TEXT         | Description or summary of content                              |
| FileURL         | VARCHAR(255) | S3 Key                                                         |
| ContentType     | VARCHAR(255) | Diffrent Type of extentions file supports                      |
| ContentLanguage | VARCHAR(50)  | Language of content                                            |
| CreatedDate     | Date         | date of created content                                        |
| UpdatedDate     | Dtae         | date of updated content                                        |

## License Table

| Column Name    | Data Type   | Description                                                   |
|----------------|-------------|---------------------------------------------------------------|
| LicenseID      | INT         | Primary key, unique license ID                                |
| ContentID      | INT         | Foreign key referencing Content.ContentID                     |
| LicenseKey     | VARCHAR(50) | Unique license key assigned to approved content               |
| LicensorUserID | INT         | Foreign key referencing PublisherId, identifies the publisher |
| LicenseeUserID | INT         | Foreign key referencing WriterId, identifies the writer       |
| IssueDate      | DATE        | Date when the license was issued                              |
| ExpiryDate     | DATE        | Expiry date of the license                                    |

## PublisherContent

| Column Name   | Data Type                               | Description                                                   |
|---------------|-----------------------------------------|---------------------------------------------------------------|
| Id            | INT                                     | Primary key, unique license ID                                |
| ContentID     | INT                                     | Foreign key referencing Content.ContentID                     |
| PublisherId   | VARCHAR(50)                             | Foreign key referencing PublisherId, identifies the publisher |
| Status        | ENUM('pending', 'approved', 'rejected') | Status of content upload                                      |
| PublishedDate | DATE                                    | Date when the license was issued                              |
| reason        | Text                                    | Action performed by publisher reason                          |


## Review Table


| Column Name    | Data Type    | Description                     |
|----------------|--------------|---------------------------------|
| ReviewID       | INT          | Primary key, unique review ID    |
| ContentID      | INT          | Foreign key referencing Content.ContentID |
| UserID         | INT          | Foreign key referencing Users.UserID, identifies the publisher |
| ReviewDate     | DATETIME     | Date and time of review          |
| Comments       | TEXT         | Reviewer's comments              |



