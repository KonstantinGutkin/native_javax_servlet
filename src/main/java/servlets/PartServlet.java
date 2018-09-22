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

        req.setAttribute("parts", partDao.getParts());
        req.getRequestDispatcher("/jsp/part.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            req.setAttribute("parts", partDao.getParts(mapToPartFilterDto(req), mapToSortDto(req)));
            req.setAttribute("sortField", req.getParameter("sortField"));
            req.setAttribute("sortOrder", req.getParameter("sortOrder"));
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
        return partFilterDto;
    }

    private SortDto mapToSortDto(HttpServletRequest request) {
        SortDto sortDto = new SortDto();
        sortDto.setField(emptyToNull(request.getParameter("sortField")));
        sortDto.setOrder(emptyToNull(request.getParameter("sortOrder")));
        return sortDto;
    }

    @SneakyThrows
    private Date getDateParameter(String date) {
        if (date == null) {
            return null;
        }
        return dateFormat.parse(date);
    }


}
