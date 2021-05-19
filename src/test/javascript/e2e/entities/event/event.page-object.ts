import { element, by, ElementFinder } from 'protractor';

export class EventComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-event div table .btn-danger'));
  title = element.all(by.css('jhi-event div h2#page-heading span')).first();
  noResult = element(by.id('no-result'));
  entities = element(by.id('entities'));

  async clickOnCreateButton(): Promise<void> {
    await this.createButton.click();
  }

  async clickOnLastDeleteButton(): Promise<void> {
    await this.deleteButtons.last().click();
  }

  async countDeleteButtons(): Promise<number> {
    return this.deleteButtons.count();
  }

  async getTitle(): Promise<string> {
    return this.title.getText();
  }
}

export class EventUpdatePage {
  pageTitle = element(by.id('jhi-event-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  titleInput = element(by.id('field_title'));
  start_dateInput = element(by.id('field_start_date'));
  end_dateInput = element(by.id('field_end_date'));
  start_timeInput = element(by.id('field_start_time'));
  end_timeInput = element(by.id('field_end_time'));
  colorInput = element(by.id('field_color'));
  is_all_dayInput = element(by.id('field_is_all_day'));
  recurring_dayInput = element(by.id('field_recurring_day'));

  calendarSelect = element(by.id('field_calendar'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getText();
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
  }

  async setTitleInput(title: string): Promise<void> {
    await this.titleInput.sendKeys(title);
  }

  async getTitleInput(): Promise<string> {
    return await this.titleInput.getAttribute('value');
  }

  async setStart_dateInput(start_date: string): Promise<void> {
    await this.start_dateInput.sendKeys(start_date);
  }

  async getStart_dateInput(): Promise<string> {
    return await this.start_dateInput.getAttribute('value');
  }

  async setEnd_dateInput(end_date: string): Promise<void> {
    await this.end_dateInput.sendKeys(end_date);
  }

  async getEnd_dateInput(): Promise<string> {
    return await this.end_dateInput.getAttribute('value');
  }

  async setStart_timeInput(start_time: string): Promise<void> {
    await this.start_timeInput.sendKeys(start_time);
  }

  async getStart_timeInput(): Promise<string> {
    return await this.start_timeInput.getAttribute('value');
  }

  async setEnd_timeInput(end_time: string): Promise<void> {
    await this.end_timeInput.sendKeys(end_time);
  }

  async getEnd_timeInput(): Promise<string> {
    return await this.end_timeInput.getAttribute('value');
  }

  async setColorInput(color: string): Promise<void> {
    await this.colorInput.sendKeys(color);
  }

  async getColorInput(): Promise<string> {
    return await this.colorInput.getAttribute('value');
  }

  getIs_all_dayInput(): ElementFinder {
    return this.is_all_dayInput;
  }

  async setRecurring_dayInput(recurring_day: string): Promise<void> {
    await this.recurring_dayInput.sendKeys(recurring_day);
  }

  async getRecurring_dayInput(): Promise<string> {
    return await this.recurring_dayInput.getAttribute('value');
  }

  async calendarSelectLastOption(): Promise<void> {
    await this.calendarSelect.all(by.tagName('option')).last().click();
  }

  async calendarSelectOption(option: string): Promise<void> {
    await this.calendarSelect.sendKeys(option);
  }

  getCalendarSelect(): ElementFinder {
    return this.calendarSelect;
  }

  async getCalendarSelectedOption(): Promise<string> {
    return await this.calendarSelect.element(by.css('option:checked')).getText();
  }

  async save(): Promise<void> {
    await this.saveButton.click();
  }

  async cancel(): Promise<void> {
    await this.cancelButton.click();
  }

  getSaveButton(): ElementFinder {
    return this.saveButton;
  }
}

export class EventDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-event-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-event'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
