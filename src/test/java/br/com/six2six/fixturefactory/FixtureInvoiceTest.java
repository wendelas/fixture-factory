package br.com.six2six.fixturefactory;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.model.Invoice;
import br.com.six2six.fixturefactory.util.DateTimeUtil;

public class FixtureInvoiceTest {

	@Before
	public void setUp() {
		Fixture.of(Invoice.class).addTemplate("valid", new Rule(){{
			add("id", regex("\\d{3,5}"));
			add("ammount", random(new BigDecimal("58.67"), new BigDecimal("45.89")));
			add("dueDate", beforeDate("2011-04-08", new SimpleDateFormat("yyyy-MM-dd")));
			
		}}).addTemplate("previousInvoices", new Rule() {{
			add("id", regex("\\d{3,5}"));
			add("ammount", random(new BigDecimal("58.67"), new BigDecimal("45.89")));
			add("dueDate", sequenceDate("2011-04-01", new SimpleDateFormat("yyyy-MM-dd"), decrement(1).month()));
			
		}}).addTemplate("nextInvoices", new Rule() {{
			add("id", regex("\\d{3,5}"));
			add("ammount", random(new BigDecimal("58.67"), new BigDecimal("45.89")));
			add("dueDate", sequenceDate("2011-04-30", increment(1).day()));
		}});
	}

	@Test
	public void fixtureInvoice() {
		Invoice invoice = Fixture.from(Invoice.class).gimme("valid");
		assertNotNull("Invoice should not be null", invoice);
	}
	
	@Test
	public void fixturePreviousInvoices() {
		List<Invoice> invoices = Fixture.from(Invoice.class).gimme(3, "previousInvoices");
		assertNotNull("Invoice list should not be null", invoices);
		assertTrue("Invoice list should not be empty", !invoices.isEmpty());
		
		Calendar calendar = DateTimeUtil.toCalendar("2011-04-01", new SimpleDateFormat("yyyy-MM-dd"));
		
		for (Invoice invoice : invoices) {
			assertEquals("Calendar should be equal", calendar, invoice.getDueDate());
			calendar.add(Calendar.MONTH, -1);
		}
	}
	
	@Test
	public void fixtureNextInvoices() {
		List<Invoice> invoices = Fixture.from(Invoice.class).gimme(3, "nextInvoices");
		assertNotNull("Invoice list should not be null", invoices);
		assertTrue("Invoice list should not be empty", !invoices.isEmpty());
		
		Calendar calendar = DateTimeUtil.toCalendar("2011-04-30", new SimpleDateFormat("yyyy-MM-dd"));
		
		for (Invoice invoice : invoices) {
			assertEquals("Calendar should be equal", calendar, invoice.getDueDate());
			calendar.add(Calendar.DAY_OF_MONTH, 1);
		}
	}
}
