import { element, by, ElementFinder } from 'protractor';

export class TeamComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-team div table .btn-danger'));
  title = element.all(by.css('jhi-team div h2#page-heading span')).first();
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

export class TeamUpdatePage {
  pageTitle = element(by.id('jhi-team-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  nameInput = element(by.id('field_name'));

  calendarSelect = element(by.id('field_calendar'));
  leaderSelect = element(by.id('field_leader'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getText();
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
  }

  async setNameInput(name: string): Promise<void> {
    await this.nameInput.sendKeys(name);
  }

  async getNameInput(): Promise<string> {
    return await this.nameInput.getAttribute('value');
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

  async leaderSelectLastOption(): Promise<void> {
    await this.leaderSelect.all(by.tagName('option')).last().click();
  }

  async leaderSelectOption(option: string): Promise<void> {
    await this.leaderSelect.sendKeys(option);
  }

  getLeaderSelect(): ElementFinder {
    return this.leaderSelect;
  }

  async getLeaderSelectedOption(): Promise<string> {
    return await this.leaderSelect.element(by.css('option:checked')).getText();
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

export class TeamDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-team-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-team'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
