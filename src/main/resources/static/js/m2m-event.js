class M2mEvent {
    constructor(sourceData, input, name) {
        this.sourceData = sourceData
        this.input = input
        this.name = name
        console.log(this)
    }


    saveSourceData() {
        const json = JSON.stringify(this.sourceData)
        console.log(json)
        this.input.val(json)
    }

    updateRemoveEvent() {
        const removeBtn = $(`.m2m-remove-btn-${this.name}`)
        removeBtn.off('click')
        const that = this
        removeBtn.click(function () {
            const id = $(this).data('m2m-id')
            const [, rowId] = id.split('-')
            that.sourceData[+rowId] = {}
            $(`#${id}`).remove()
            that.saveSourceData()
            that.updateSequences()
        })
    }


    updateEditEvent() {
        const inputField = $(`.m2m-input-${this.name}`)
        inputField.off('input')
        const that = this
        inputField.on('input', function () {
            const [, rowId, colName] = $(this).data('m2m-id').split('-')
            that.sourceData[+rowId][colName] = $(this).val().toString()
            that.saveSourceData()
        })
    }

    updateSequences() {
        const sequenceInputs = $(`.m2m-input-${this.name}.m2m-sequence`)
        const that = this
        sequenceInputs.each(function (index) {
            const [, rowId, colName] = $(this).data('m2m-id').split('-')
            that.sourceData[+rowId][colName] = String(index + 1)
            $(this).val(index + 1)
            that.saveSourceData()
        })
    }

    updateSwapSequenceEvent() {
        const swapUpButtons = $(`.m2m-seq-up-btn-${this.name}`)
        const swapDownButtons = $(`.m2m-seq-down-btn-${this.name}`)
        swapUpButtons.off('click')
        swapDownButtons.off('click')
        const that = this
        swapUpButtons.click(function () {
            const currentRow = $(this).closest('tr')
            const prevRow = currentRow.prev()
            console.log("Prev ", prevRow, currentRow)
            if (!$.isEmptyObject(prevRow)) {
                prevRow.insertAfter(currentRow)
            }
            that.updateSequences()
        })
        swapDownButtons.click(function () {
            const currentRow = $(this).closest('tr')
            const nextRow = currentRow.next()
            if (!$.isEmptyObject(nextRow)) {
                currentRow.insertAfter(nextRow)
            }
            that.updateSequences()
        })
    }


    updateEvents() {
        this.updateRemoveEvent()
        this.updateEditEvent()
        this.updateSequences()
        this.updateSwapSequenceEvent()
    }
}