package ferwafa.common;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebFilter(filterName = "AuthFilter", urlPatterns = { "*.xhtml" })
public class AuthorizationFilter implements Filter,DbConstant  {
	private static final Logger LOGGER = Logger.getLogger(Thread.currentThread().getStackTrace()[0].getClassName());
	public AuthorizationFilter() {
	}

	public void init(FilterConfig filterConfig) throws ServletException {

	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		try {

			HttpServletRequest reqt = (HttpServletRequest) request;
			HttpServletResponse resp = (HttpServletResponse) response;
			HttpSession ses = reqt.getSession(false);

			String reqURI = reqt.getRequestURI();
			if ((reqURI.indexOf("contact.xhtml") >= 0||reqURI.indexOf("about.xhtml") >= 0||reqURI.indexOf("index.xhtml") >= 0 ||reqURI.indexOf("login.xhtml") >= 0 ||reqURI.indexOf(HOMEURL) >= 0) || (ses != null && ses.getAttribute(USERSESSION) != null)
					|| reqURI.indexOf(PUBLICPATH) >= 0 || reqURI.contains(JAVAFACERESOURCE))
				chain.doFilter(request, response);
			else
				resp.sendRedirect(reqt.getContextPath() + HOMEURL);
		} catch (Exception e) {
			LOGGER.info(e.getMessage());
		}
	}

	public void destroy() {

	}
}