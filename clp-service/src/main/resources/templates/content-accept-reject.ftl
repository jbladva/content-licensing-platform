<#if action == "accept">
    <html>
    <head>
        <title>Content Approval Notification</title>
    </head>
    <body>
    <p>Dear ${receiver},</p>

    <p>We are pleased to inform you that the content submitted for approval has been reviewed and
        <strong>approved</strong>.</p>

    <p>Details:</p>
    <ul>
        <li><strong>Title:</strong> ${contentTitle}</li>
        <li><strong>Author:</strong> ${writerName}</li>
        <li><strong>Publisher Name:</strong> ${publisherName}</li>
        <li><strong>Approval Date:</strong> ${approvalDate}</li>
    </ul>

    <p>Thank you for your attention to this matter.</p>

    </body>
    </html>
<#else>
<#-- Email template for rejection -->
    <html>
    <head>
        <title>Content Rejection Notification</title>
    </head>
    <body>
    <p>Dear ${receiver},</p>

    <p>We regret to inform you that the content submitted for approval has been <strong>rejected</strong>.</p>

    <p>Details:</p>
    <ul>
        <li><strong>Title:</strong> ${contentTitle}</li>
        <li><strong>Author:</strong> ${writerName}</li>
        <li><strong>Publisher Name:</strong> ${publisherName}</li>
    </ul>

    <p>Please feel free to contact us for further clarification.</p>

    </body>
    </html>
</#if>
