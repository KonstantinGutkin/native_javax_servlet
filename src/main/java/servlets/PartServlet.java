package servlets;

import dao.PartDao;
import dto.PartFilterDto;
import dto.SortDto;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static java.util.Optional.ofNullable;
import static utils.StringUtils.emptyToNull;

@Slf4j
@WebServlet("/part")
public class PartServlet extends HttpServlet {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH);

    private PartDao partDao = new PartDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html;charset=utf-8");

        try {
            PartFilterDto partFilterDto = mapToPartFilterDto(req);
            req.setAttribute("parts", partDao.getParts(partFilterDto));
            req.setAttribute("partFilter", partFilterDto);
            req.getRequestDispatcher("/jsp/part.jsp").forward(req, resp);
        } catch (SQLException e) {
            log.error("Error when getting parts", e);
        }
    }


    private PartFilterDto mapToPartFilterDto(HttpServletRequest request) {
        PartFilterDto partFilterDto = new PartFilterDto();
        partFilterDto.setNumber(emptyToNull(request.getParameter("number")));
        partFilterDto.setName(emptyToNull(request.getParameter("name")));
        partFilterDto.setVendor(emptyToNull(request.getParameter("vendor")));
        partFilterDto.setQty(
                ofNullable(emptyToNull(request.getParameter("qty"))).map(Integer::valueOf).orElse(null)
        );
        partFilterDto.setShippedAfter(getDateParameter(emptyToNull(request.getParameter("shippedAfter"))));
        partFilterDto.setShippedBefore(getDateParameter(emptyToNull(request.getParameter("shippedBefore"))));
        partFilterDto.setReceivedAfter(
                getDateParameter(request.getParameter(emptyToNull(request.getParameter("receivedAfter"))))
        );
        partFilterDto.setReceivedBefore(getDateParameter(emptyToNull(request.getParameter("receivedBefore"))));
        partFilterDto.setSortField(emptyToNull(request.getParameter("sortField")));
        partFilterDto.setSortOrder(emptyToNull(request.getParameter("sortOrder")));
        return partFilterDto;
    }

    @SneakyThrows
    private Date getDateParameter(String date) {
        if (date == null) {
            return null;
        }
        return dateFormat.parse(date);
    }


}
