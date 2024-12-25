<#if action == "published">
    <html>
    <head>
        <title>Content Published Notification</title>
    </head>
    <body>
    <p>Hello ${receiver},</p>
    <p>Your content has been published. Congratulations!</p>
    <p>Best regards,<br/>The Publisher</p>
    </body>
    </html>
<#elseif action == "unpublished">
    <html>
    <head>
        <title>Content Unpublished Notification</title>
    </head>
    <body>
    <p>Hello ${receiver},</p>
    <p>We regret to inform you that your content has been unpublished.</p>
    <p>Best regards,<br/>The Publisher</p>
    </body>
    </html>
</#if>
