$(document).ready(function () {
    function dateRangeHandler(start, end, label) {
        const from = start.format('YYYY-MM-DD')
        const to = end.format('YYYY-MM-DD')
        const currentUrl = window.location.origin + window.location.pathname
        window.location.replace(`${currentUrl}?from=${from}&to=${to}`)
    }

    function getCurrentDate() {
        const search = window.location.search || ''
        if (!search) {
            return ''
        }
        let [from, to] = search.slice(1).split('&').map(q => q.split('=')[1])
        from = moment(from, "YYYY-MM-DD").format('DD/MM/YYYY')
        to = moment(to, "YYYY-MM-DD").format('DD/MM/YYYY')
        return `${from} - ${to}`
    }

    const inputDateRange = $('input.date-range')

    inputDateRange.val(getCurrentDate())

    inputDateRange.daterangepicker({
        opens: 'left', autoUpdateInput: false,
        locale: {
            cancelLabel: 'Clear'
        }
    }, dateRangeHandler);

    inputDateRange.next().find('button').click(function () {
        const currentUrl = window.location.origin + window.location.pathname
        window.location.replace(currentUrl);
    })
});
