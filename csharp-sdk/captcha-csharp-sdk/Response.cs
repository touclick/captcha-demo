namespace MD5Sign
{
    public class Response
    {

        private  const long serialVersionUID = -176092625883595547L;
        private const int OK = 200;// OK: Success!
        private const int NOT_MODIFIED = 304;// Not Modified: There was no new data to return.
        private const int BAD_REQUEST = 400;// Bad Request: The request was invalid.  An accompanying error message will explain why. This is the status code will be returned during rate limiting.
        private const int NOT_AUTHORIZED = 401;// Not Authorized: Authentication credentials were missing or incorrect.
        private const int FORBIDDEN = 403;// Forbidden: The request is understood, but it has been refused.  An accompanying error message will explain why.
        private const int NOT_FOUND = 404;// Not Found: The URI requested is invalid or the resource requested, such as a user, does not exists.
        private const int NOT_ACCEPTABLE = 406;// Not Acceptable: Returned by the Search API when an invalid format is specified in the request.
        private const int INTERNAL_SERVER_ERROR = 500;// Internal Server Error: Something is broken.  Please post to the group so the Touclick team can investigate.
        private const int BAD_GATEWAY = 502;// Bad Gateway: Touclick is down or being upgraded.
        private const int SERVICE_UNAVAILABLE = 503;// Service Unavailable: The Touclick servers are up, but overloaded with requests. Try again later. The search and trend methods use this to indicate when you are being rate limited.
        public int status { get; set; }

        public string info { get; set; }

        public Response()
        {
        }

        public Response(int status, string info)
        {
            this.status = status;
            this.info = info;
        }

        public static string getCause(int statusCode)
        {
            string cause = null;
            switch (statusCode)
            {
                case NOT_MODIFIED:
                    break;
                case BAD_REQUEST:
                    cause = "The request was invalid.  An accompanying error message will explain why. This is the status code will be returned during rate limiting.";
                    break;
                case NOT_AUTHORIZED:
                    cause = "Authentication credentials were missing or incorrect.";
                    break;
                case FORBIDDEN:
                    cause = "The request is understood, but it has been refused.  An accompanying error message will explain why.";
                    break;
                case NOT_FOUND:
                    cause = "The URI requested is invalid or the resource requested, such as a user, does not exists.";
                    break;
                case NOT_ACCEPTABLE:
                    cause = "Returned by the Search API when an invalid format is specified in the request.";
                    break;
                case INTERNAL_SERVER_ERROR:
                    cause = "Something is broken.  Please post to the group so the Touclick team can investigate.";
                    break;
                case BAD_GATEWAY:
                    cause = "Touclick is down or being upgraded.";
                    break;
                case SERVICE_UNAVAILABLE:
                    cause = "Service Unavailable: The Touclick servers are up, but overloaded with requests. Try again later. The search and trend methods use this to indicate when you are being rate limited.";
                    break;
                default:
                    cause = "";
                    break;
            }
            return statusCode + ":" + cause;
        }
    }
}