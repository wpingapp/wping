package wping.filter;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;

/**
 * Servlet Filter implementation class Filter
 */
@WebFilter("/*")
public class Filter implements javax.servlet.Filter {

	private static Set<String> connections = Collections.synchronizedSet(new HashSet<String>());
	
	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {

	}

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {

	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

		String info = request.getRemoteAddr();
		if (request instanceof HttpServletRequest) {
			info = info + " " + ((HttpServletRequest)request).getHeader("User-Agent");
		}

		if (!connections.contains(info)) {
			connections.add(info);
			System.out.println(info);
		}
		
		chain.doFilter(request, response);
	}


}
